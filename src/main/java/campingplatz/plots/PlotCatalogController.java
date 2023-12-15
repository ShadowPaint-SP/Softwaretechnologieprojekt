package campingplatz.plots;

import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import campingplatz.utils.Pair;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.salespointframework.core.Currencies.EURO;

@Controller
@SessionAttributes("plotCart")
class PlotCatalogController {

    PlotCatalog plotCatalog;
    PlotReservationRepository reservationRepository;

    PlotCatalogController(PlotCatalog plotCatalog, PlotReservationRepository reservationRepository) {
        this.plotCatalog = plotCatalog;
        this.reservationRepository = reservationRepository;
    }


	// interface representing the state of the plot catalog site
	// most notably the data of the plot filter query
	interface SiteState {

		////////////////////
		// plot filter query

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate getArrival();

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate getDeparture();

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		default LocalDate getDefaultedArrival() {
			if (getArrival() == null) {
				return LocalDate.now();
			}
			return getArrival();
		}

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		default LocalDate getDefaultedDeparture() {
			if (getDeparture() == null) {
				return LocalDate.now().plusDays(1);
			}
			return getDeparture();
		}

		// used in plotcatalog.html for min arrival date.
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		default LocalDate minArrivalDate() {
			return LocalDate.now();
		}

		Double getSizeMin();

		Double getSizeMax();

		Double getPriceMin();

		Double getPriceMax();

		Integer getParking();

		////////////////////
		// other site data

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate getFirstWeekDate();

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		void setFirstWeekDate(LocalDate value);

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		default LocalDate getDefaultedFirstWeekDate() {
			if (getFirstWeekDate() == null) {
				var weekDay = getDefaultedArrival().getDayOfWeek().getValue() - 1;
				var weekBegin = getDefaultedArrival().minusDays(weekDay);
				return weekBegin;
			}
			return getFirstWeekDate();
		}

	}



    @ModelAttribute("plotCart")
    PlotCart initializeCart() {
        return new PlotCart();
    }

    @GetMapping("/plotcatalog") // consider renaming the query argument and attribute to state
    String setupCatalog(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalogController.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekDate = query.getDefaultedFirstWeekDate();
        var lastWeekDay = firstWeekDate.plusDays(7);
        var rawWeekDates = firstWeekDate.datesUntil(lastWeekDay);
        var formatedWeekDates = rawWeekDates.map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM"))).toList();

        var operationalPlots = plotCatalog.findByState(Plot.State.OPERATIONAL);
        var reservedPlots = reservationRepository.findPlotsReservedBetween(
                query.getDefaultedArrival().atStartOfDay(), query.getDefaultedDeparture().atStartOfDay()
		);
		var availablePlots = operationalPlots.stream().filter(plot ->
			plot.getClass().equals(Plot.class) && !reservedPlots.contains(plot)
		).toList();
		var evaluatedPlots = PlotCatalogController.evaluatePlots(availablePlots, query);
		var filteredPlots = filterHits(evaluatedPlots);
		var aproximatlyFilteredPlots = aproximateHits(evaluatedPlots);


        var reservations = reservationRepository.findReservationsBetween(firstWeekDate.atStartOfDay(),
                lastWeekDay.atStartOfDay());
        var availabilityTable = new PlotCatalogAvailabilityTable(firstWeekDate, lastWeekDay, availablePlots)
                .addReservations(user, reservations)
                .addHighlights(query, reservedPlots)
                .addSelections(reservationCart)
                .collapse();

        model.addAttribute("filteredPlots", filteredPlots);
		model.addAttribute("aproximatlyFilteredPlots", aproximatlyFilteredPlots);
        model.addAttribute("availabilityTable", availabilityTable);
        model.addAttribute("searchQuery", query);
        model.addAttribute("weekDates", formatedWeekDates);

        return "servings/plotcatalog";
    }

    @PostMapping("/plotcatalog/filter")
    String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalogController.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {
        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/next-week")
    String nextWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalogController.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.plusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/prev-week")
    String prevWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalogController.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.minusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}")
    @PreAuthorize("isAuthenticated()")
    String addReservationRange(Model model, @LoggedIn UserAccount user, @Valid PlotCatalogController.SiteState query,
            @PathVariable Plot plot, @ModelAttribute("plotCart") PlotCart reservationCart) {

        var arrival = query.getArrival().atStartOfDay();
        var departure = query.getDeparture().atStartOfDay();
        var reservation = new PlotReservation(user, plot, arrival, departure);
        reservationCart.add(reservation);

        return setupCatalog(model, Optional.ofNullable(user), query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}/{index}")
    @PreAuthorize("isAuthenticated()")
    String addReservationDay(Model model, @LoggedIn UserAccount user, @Valid PlotCatalogController.SiteState query,
            @PathVariable("plot") Plot plot, @PathVariable("index") Integer index,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var day = query.getDefaultedFirstWeekDate().plusDays(index).atStartOfDay();

        if (!reservationCart.containsEntry(plot, day)) {
            reservationCart.addEntry(plot, day);
        } else {
            reservationCart.removeEntry(plot, day);
        }

        return setupCatalog(model, Optional.ofNullable(user), query, reservationCart);
    }

    @GetMapping("/seasonalplots")
    String setupSeasonalCatalog(Model model, @Valid PlotCatalogController.SiteState query) {
        model.addAttribute("searchQuery", query);
        return "servings/seasonalplotcatalog";
    }


	public static List<Pair<Integer, Plot>> evaluatePlots(List<Plot> plots, SiteState query) {

		// create a list of pairs, the first element of the pair being the amount of fails
		return plots.stream().map(plot -> {
			Integer fails = 0;

			var priceMin = query.getPriceMin();
			if (priceMin != null && plot.getPrice().isLessThan(Money.of(priceMin, EURO))) {
				fails++;
			}

			var priceMax = query.getPriceMax();
			if (priceMax != null && plot.getPrice().isGreaterThan(Money.of(priceMax, EURO))) {
				fails++;
			}

			var sizeMin = query.getSizeMin();
			if (sizeMin != null && plot.getSize() < sizeMin) {
				fails++;
			}

			var sizeMax = query.getSizeMax();
			if (sizeMax != null && plot.getSize() > sizeMax) {
				fails++;
			}

			var parkingMin = query.getParking();
			if (parkingMin != null && plot.getParking().size < parkingMin) {
				fails++;
			}

			return new Pair<Integer, Plot>(fails, plot);
		})
		// sort the list by the lowest amount of fails
		.sorted(Comparator.comparing(firstPair -> firstPair.first))
		// return as a List
		.toList();
	}

	public static List<Plot> filterHits(List<Pair<Integer, Plot>> evalautedPlots){
		return evalautedPlots.stream()
			.filter(pair -> pair.first == 0)
			.map(pair -> pair.second)
			.toList();
	}

	public static List<Plot> aproximateHits(List<Pair<Integer, Plot>> evalautedPlots){
		return evalautedPlots.stream()
			.filter(pair -> pair.first != 0)
			.map(pair -> pair.second)
			.toList();
	}




}

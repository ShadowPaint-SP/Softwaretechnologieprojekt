package campingplatz.plots;

import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("plotCart")
class PlotCatalogController {

    PlotCatalog plotCatalog;
    PlotReservationRepository reservationRepository;

    PlotCatalogController(PlotCatalog plotCatalog, PlotReservationRepository reservationRepository) {
        this.plotCatalog = plotCatalog;
        this.reservationRepository = reservationRepository;
    }

    @ModelAttribute("plotCart")
    PlotCart initializeCart() {
        return new PlotCart();
    }

    @GetMapping("/plotcatalog") // consider renaming the query argument and attribute to state
    String setupCatalog(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekDate = query.getDefaultedFirstWeekDate();
        var lastWeekDay = firstWeekDate.plusDays(7);
        var rawWeekDates = firstWeekDate.datesUntil(lastWeekDay);
        var formatedWeekDates = rawWeekDates.map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM"))).toList();

        var filteredPlots = plotCatalog.filter(query);
        var reservedPlots = reservationRepository.findPlotsReservedBetween(
                query.getDefaultedArrival().atStartOfDay(), query.getDefaultedDeparture().atStartOfDay());
        var partitionedPlots = filteredPlots.stream().collect(Collectors.partitioningBy(
                plot -> !reservedPlots.contains(plot)));

        var reservations = reservationRepository.findReservationsBetween(firstWeekDate.atStartOfDay(),
                lastWeekDay.atStartOfDay());
        var availabilityTable = new PlotCatalogAvailabilityTable(firstWeekDate, lastWeekDay, filteredPlots)
                .addReservations(user, reservations)
                .addHighlights(query, reservedPlots)
                .addSelections(reservationCart)
                .collapse();

        model.addAttribute("allPlots", partitionedPlots);
        model.addAttribute("availabilityTable", availabilityTable);
        model.addAttribute("searchQuery", query);
        model.addAttribute("weekDates", formatedWeekDates);

        return "servings/plotcatalog";
    }

    @PostMapping("/plotcatalog/filter")
    String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {
        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/next-week")
    String nextWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.plusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/prev-week")
    String prevWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("plotCart") PlotCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.minusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}")
    @PreAuthorize("isAuthenticated()")
    String addReservationRange(Model model, @LoggedIn UserAccount user, @Valid PlotCatalog.SiteState query,
            @PathVariable Plot plot, @ModelAttribute("plotCart") PlotCart reservationCart) {

        var arrival = query.getArrival().atStartOfDay();
        var departure = query.getDeparture().atStartOfDay();
        var reservation = new PlotReservation(user, plot, arrival, departure);
        reservationCart.add(reservation);

        return setupCatalog(model, Optional.ofNullable(user), query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}/{index}")
    @PreAuthorize("isAuthenticated()")
    String addReservationDay(Model model, @LoggedIn UserAccount user, @Valid PlotCatalog.SiteState query,
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
    String setupSeasonalCatalog(Model model, @Valid PlotCatalog.SiteState query) {
        model.addAttribute("searchQuery", query);
        return "servings/seasonalplotcatalog";
    }

}

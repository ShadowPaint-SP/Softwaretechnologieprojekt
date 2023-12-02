package campingplatz.plots;

import campingplatz.reservation.Reservation;
import campingplatz.reservation.ReservationCart;
import campingplatz.reservation.ReservationRepository;
import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
class PlotCatalogController {

    PlotCatalog plotCatalog;
    ReservationRepository reservationRepository;

    PlotCatalogController(PlotCatalog plotCatalog, ReservationRepository reservationRepository) {
        this.plotCatalog = plotCatalog;
        this.reservationRepository = reservationRepository;
    }

    @ModelAttribute("cart")
    ReservationCart initializeCart() {
        return new ReservationCart();
    }

    @GetMapping("/plotcatalog") // consider renaming the query argument and attribute to state
    String setupCatalog(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("cart") ReservationCart reservationCart) {

        var firstWeekDate = query.getDefaultedFirstWeekDate();
        var lastWeekDay = firstWeekDate.plusDays(7);
        var rawWeekDates = firstWeekDate.datesUntil(lastWeekDay);
        var formatedWeekDates = rawWeekDates.map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM"))).toList();

        var filteredPlots = plotCatalog.filter(query);
        var reservedPlots = reservationRepository.findPlotsReservedBetween(
                query.getDefaultedArrival(), query.getDefaultedDeparture());
        var partitionedPlots = filteredPlots.stream().collect(Collectors.partitioningBy(
                plot -> !reservedPlots.contains(plot)));

        var reservations = reservationRepository.findReservationsBetween(firstWeekDate, lastWeekDay);
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
            @ModelAttribute("cart") ReservationCart reservationCart) {
        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/next-week")
    String nextWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("cart") ReservationCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.plusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/prev-week")
    String prevWeek(Model model, @LoggedIn Optional<UserAccount> user, @Valid PlotCatalog.SiteState query,
            @ModelAttribute("cart") ReservationCart reservationCart) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.minusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, user, query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}")
    @PreAuthorize("isAuthenticated()")
    String addReservationRange(Model model, @LoggedIn UserAccount user, @Valid PlotCatalog.SiteState query,
            @PathVariable Plot plot, @ModelAttribute("cart") ReservationCart reservationCart) {

        var reservation = new Reservation(user, plot, query.getArrival(), query.getDeparture());
        reservationCart.add(reservation);

        return setupCatalog(model, Optional.ofNullable(user), query, reservationCart);
    }

    @PostMapping("/plotcatalog/select/{plot}/{index}")
    @PreAuthorize("isAuthenticated()")
    String addReservationDay(Model model, @LoggedIn UserAccount user, @Valid PlotCatalog.SiteState query,
            @PathVariable("plot") Plot plot, @PathVariable("index") Integer index,
            @ModelAttribute("cart") ReservationCart reservationCart) {

        var day = query.getDefaultedFirstWeekDate().plusDays(index);
        var reservation = new Reservation(user, plot, day, day);

        if (reservationCart.intersecting(reservation).isEmpty()) {
            reservationCart.add(reservation);
        } else {
            reservationCart.remove(reservation);
        }

        return setupCatalog(model, Optional.ofNullable(user), query, reservationCart);
    }

    @GetMapping("/seasonalplots")
    String setupSeasonalCatalog(Model model, @Valid PlotCatalog.SiteState query) {
        var x = plotCatalog.findByType(Plot.PlotType.SEASONAL);
        model.addAttribute("allSeasonalPlots", x);
        model.addAttribute("searchQuery", query);
        return "seasonalplotcatalog";
    }

    @GetMapping("/management/plots")
    String plots(Model model) {
        Streamable<Plot> all = plotCatalog.findAll();
        model.addAttribute("Plots", all);
        return "dashboards/plot_management";
    }
}

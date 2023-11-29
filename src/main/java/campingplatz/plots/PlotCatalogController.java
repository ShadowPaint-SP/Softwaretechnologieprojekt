package campingplatz.plots;

import campingplatz.reservation.ReservationRepository;
import campingplatz.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
class PlotCatalogController {

    PlotCatalog plotCatalog;
    ReservationRepository reservationRepository;

    PlotCatalogController(PlotCatalog plotCatalog, ReservationRepository reservationRepository) {
        this.plotCatalog = plotCatalog;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/plots") // consider renaming the query argument and attribute to state
    String setupCatalog(Model model, @Valid PlotCatalog.SiteState query) {

        var firstDay = query.getDefaultedFirstWeekDate();
        var lastDay = firstDay.plusDays(7);
        var rawWeekDates = firstDay.datesUntil(lastDay);
        var formatedWeekDates = rawWeekDates.map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM"))).toList();

        var filteredPlots = plotCatalog.filter(query);
        var reservedPlotIds = reservationRepository.findPlotsReservedBetween(
                query.getDefaultedArrival(), query.getDefaultedDeparture());
        var partitionedPlots = filteredPlots.stream().collect(Collectors.partitioningBy(
                plot -> !reservedPlotIds.contains(plot)));

        var reservations = reservationRepository.findReservationsBetween(firstDay, lastDay);
        var availabilityTable = Utils.constructAvailabilityTable(firstDay, lastDay, filteredPlots, reservations);

        model.addAttribute("allPlots", partitionedPlots);
        model.addAttribute("availabilityTable", availabilityTable);
        model.addAttribute("searchQuery", query);
        model.addAttribute("weekDates", formatedWeekDates);

        return "servings/plotcatalog";
    }

    @PostMapping("/plots")
    String filter(Model model, @Valid PlotCatalog.SiteState query) {
        return setupCatalog(model, query);
    }

    @PostMapping("/plots/next-week")
    String nextWeek(Model model, @Valid PlotCatalog.SiteState query) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.plusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, query);
    }

    @PostMapping("/plots/prev-week")
    String prevWeek(Model model, @Valid PlotCatalog.SiteState query) {

        var firstWeekday = query.getDefaultedFirstWeekDate();
        var firstWeekdayNextWeek = firstWeekday.minusWeeks(1);
        query.setFirstWeekDate(firstWeekdayNextWeek);

        return setupCatalog(model, query);
    }

}

/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package campingplatz.plots;

import campingplatz.reservation.ReservationRepository;
import campingplatz.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
			          query.getDefaultedArrival(), query.getDefaultedDeparture()
		    );
        var partitionedPlots = filteredPlots.stream().collect(Collectors.partitioningBy(
                plot -> !reservedPlotIds.contains(plot))
        );


        var reservations = reservationRepository.findReservationsBetween(firstDay, lastDay);
        var availabilityTable = Utils.constructAvailabilityTable(firstDay, lastDay, filteredPlots, reservations);

        model.addAttribute("allPlots", partitionedPlots);
        model.addAttribute("availabilityTable", availabilityTable);
        model.addAttribute("searchQuery", query);
        model.addAttribute("weekDates", formatedWeekDates);

        return "plotcatalog";
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

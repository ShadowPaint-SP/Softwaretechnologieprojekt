package campingplatz.utils;

import campingplatz.plots.Plot;
import campingplatz.reservation.Reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class Utils {

    // hide constructor
    private Utils() {
    }

    public static Map<Plot, Boolean[]> constructAvailabilityTable(
            LocalDate firstDay,
            LocalDate lastDay,
            List<Plot> plots,
            List<Reservation> reservations) {
        int length = (int) (ChronoUnit.DAYS.between(firstDay, lastDay));

        Map<Plot, Boolean[]> availabilityTable = new HashMap<>();

        for (var plot : plots) {
            var row = new Boolean[length];
            Arrays.fill(row, true);
            availabilityTable.put(plot, row);
        }

        for (var reservation : reservations) {
            // get the row, corresponding to the plotId of the current reservation
            // if there is no such row, create one
            var row = availabilityTable.get(reservation.getPlot());
            if (row == null) {
                row = new Boolean[length];
                Arrays.fill(row, true);
                availabilityTable.put(reservation.getPlot(), row);
            }

            // calculate begin and end index.
            // we do this, because we need numbers relative to zero for indexing into an
            // array

            int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, reservation.getArrival())));
            int endIndex = (int) Math.min(length, (ChronoUnit.DAYS.between(firstDay, reservation.getDeparture())));

            for (int i = beginIndex; i < endIndex; i++) {
                row[i] = false;
            }

        }

        return availabilityTable;

    }

    @GetMapping("/contents/test")
    public String showTestPage(Model model) {
        model.addAttribute("currentFragment", "test");
        return "contetns/test";
    }

}

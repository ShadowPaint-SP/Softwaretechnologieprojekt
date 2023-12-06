package campingplatz.equip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalogAvailabilityTable;
import campingplatz.plots.PlotCatalogAvailabilityTable.FieldType;

public class SportItemAvailability extends HashMap<SportItem, SportItemAvailability[]>{
    

    LocalDate startTime;
	LocalDate endTime;
    LocalDate time;
	Integer length;


    HashMap<LocalDate, Integer> fussballHeute = new HashMap<>();

	SportItemAvailability(LocalDate startTime, LocalDate endTime, List<SportItem> items) {
		this.length = (int) (ChronoUnit.HOURS.between(startTime, endTime));
		this.startTime  = endTime;
		this.endTime = endTime;

		// create a row in the table for every plot in the
		for (var plot : items) {
			var row = new FieldType[length];
			Arrays.fill(row, FieldType.FREE_COMPLETELY);
			this.put(plot, row);        
		}
	}
}

package campingplatz.plots;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.useraccount.UserAccount;

import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;

/**
 * Represents the mapping from a given plot, to the seven elements of its
 * row in the plot catalog table.
 * <p>
 * This class implements a builder-esque pattern to split a function
 * constructing it into multiple smaller functions for practicality.
 * they should be called in order that they are declared here
 */
public class PlotCatalogAvailabilityTable extends HashMap<Plot, ArrayList<PlotCatalogAvailabilityTable.Field>> {



    public static enum FieldType {
        FREE_COMPLETELY (0, "catalog.fields.free.completely" , "bg-transparent", true, false),
        FREE_HIGHLIGHTED(1, "catalog.fields.free.highlighted", "bg-blue-300"   , true, true),
        FREE_SELECTED   (2, "catalog.fields.free.selected"   , "bg-green-500"  , true, false),
        RESERVED_OTHER  (3, "catalog.fields.reserved.other"  , "bg-red-500"    , false, false),
        RESERVED_SELF   (4, "catalog.fields.reserved.self"   , "bg-yellow-600" , false, false),
        BACK_IN_TIME    (5, "catalog.fields.past"            , "bg-red-500"    , false, false);


        public final Integer value;
        public final String label;
        public final String color;
        public final String clickability;
        public final String highlight;

        FieldType(Integer value, String label, String color, Boolean clickability, Boolean highlighted) {
            this.value = value;
            this.label = label + ".label";
            this.color = color;
            this.clickability = clickability ? "submit" : "button";
            this.highlight = highlighted ? " highlight" : "";
            //          note the space here ^ important as html classes are space seperated
        }
    }

	@AllArgsConstructor
	public static class Field {
		@Getter
		@Setter
		FieldType type;

		@Getter
		@Setter
		Integer index;

	}

	// just some "global" variables stored for convenience
	LocalDate firstDay;
	LocalDate lastDay;
	Integer length;

	public PlotCatalogAvailabilityTable(LocalDate firstDay, LocalDate lastDay, List<Plot> plots) {
		this.length = (int) (ChronoUnit.DAYS.between(firstDay, lastDay));
		this.firstDay = firstDay;
		this.lastDay = lastDay;

		// create a row in the table for every plot in the
		for (var plot : plots) {
			var row = new ArrayList<Field>();
			for (int i = 0; i < length; i++) {
				var prototype = new Field(FieldType.FREE_COMPLETELY, i);
				row.add(prototype);
			}
			this.put(plot, row);
		}
	}

	/** Marks all the periods in which the plot is reserved as such in the table */
	public PlotCatalogAvailabilityTable addReservations(Optional<UserAccount> user,
			List<PlotReservation> reservations) {
		// fill the table with reservation information
		for (var reservation : reservations) {

			// get the row, corresponding to the plotId of the current reservation
			var row = this.get(reservation.getProduct());
			if (row == null) {
				continue;
			}

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, reservation.getBegin())));
			int endIndex = (int) Math.min((long) length - 1, (ChronoUnit.DAYS.between(firstDay, reservation.getEnd())));

			for (int i = beginIndex; i <= endIndex; i++) {
				if (user.isEmpty() || reservation.getUser() != user.get()) {
					var prototype = new Field(FieldType.RESERVED_OTHER, i);
					row.set(i, prototype);
				} else {
					var prototype = new Field(FieldType.RESERVED_SELF, i);
					row.set(i, prototype);
				}
			}
		}

		return this;
	}

	/**
	 * Marks the periods between the arrival and deparure in the filter as
	 * highlighted in the table
	 */
	public PlotCatalogAvailabilityTable addHighlights(PlotCatalogController.SiteState query, Set<Plot> reservedPlots) {
		// fill the table with selections
		for (var entry : this.entrySet()) {

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, query.getDefaultedArrival())));
			int endIndex = (int) Math.min((long) length - 1,
					(ChronoUnit.DAYS.between(firstDay, query.getDefaultedDeparture())));

			var row = entry.getValue();
			if (row == null) {
				continue;
			}

			// check that it is completly free
			if (reservedPlots.contains(entry.getKey())) {
				continue;
			}

			for (int i = beginIndex; i <= endIndex; i++) {
				var prototype = new Field(FieldType.FREE_HIGHLIGHTED, i);
				row.set(i, prototype);
			}
		}

		return this;
	}

	/** Marks the periods between of reservations in the cart as selected */
	public PlotCatalogAvailabilityTable addSelections(PlotCart reservationCart) {

		for (var field : reservationCart) {
			var time = field.getTime();
			var plot = field.getProduct();

			if (time.isBefore(firstDay.atStartOfDay()) || time.isAfter(lastDay.minusDays(1).atStartOfDay())) {
				continue;
			}

			int index = (int) (ChronoUnit.DAYS.between(firstDay, time));

			var row = this.get(plot);
			if (row == null) {
				continue;
			}

			var prototype = new Field(FieldType.FREE_SELECTED, index);
			row.set(index, prototype);
		}

		return this;
	}

	public PlotCatalogAvailabilityTable addPastMarkings(LocalDate cutofTime) {

		for (var row : this.values()) {
			for (int i = 0; i < length; i++) {
				var currentTime = firstDay.plusDays(i);
				var currentField = row.get(i);

				if (currentTime.isBefore(cutofTime) && currentField.type == FieldType.FREE_COMPLETELY) {
					var prototype = new Field(FieldType.BACK_IN_TIME, i);
					row.set(i, prototype);
				}

			}
		}

		return this;
	}

}
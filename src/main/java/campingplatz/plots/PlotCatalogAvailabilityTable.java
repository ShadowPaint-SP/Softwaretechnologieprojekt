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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class PlotCatalogAvailabilityTable extends HashMap<Plot, PlotCatalogAvailabilityTable.FieldType[]> {

	public static enum FieldType {
		FREE_COMPLETELY(0, "catalog.fields.free.completely", "submit", "bg-transparent"),
		FREE_HIGHLIGHTED(1, "catalog.fields.free.highlighted", "submit", "bg-blue-300"),
		FREE_SELECTED(2, "catalog.fields.free.selected", "submit", "bg-green-500"),
		RESERVED_OTHER(3, "catalog.fields.reserved.other", "button", "bg-red-500"),
		RESERVED_SELF(4, "catalog.fields.reserved.self", "button", "bg-yellow-600");

		public final String clickability;
		public final Integer value;
		public final String css;
		public final String label;
		public final String color;

		FieldType(Integer size, String arg, String clickability, String color) {
			this.value = size;
			this.css = arg + ".css";
			this.label = arg + ".label";
			this.clickability = clickability;
			this.color = color;
		}

	}

	@AllArgsConstructor
	public static class Fields {
		@Getter
		@Setter
		FieldType type;

		@Getter
		@Setter
		Integer index;

		@Getter
		@Setter
		Integer amount;
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
			var row = new FieldType[length];
			Arrays.fill(row, FieldType.FREE_COMPLETELY);
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
					row[i] = FieldType.RESERVED_OTHER;
				} else {
					row[i] = FieldType.RESERVED_SELF;
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

			// check that it is completly free
			if (reservedPlots.contains(entry.getKey())) {
				continue;
			}

			for (int i = beginIndex; i <= endIndex; i++) {
				row[i] = FieldType.FREE_HIGHLIGHTED;
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
			row[index] = FieldType.FREE_SELECTED;
		}

		return this;
	}

	// currently not used
	public Map<Plot, List<Fields>> collapse() {

		Map<Plot, List<Fields>> ret = new HashMap<>();

		for (var entry : this.entrySet()) {
			List<Fields> collapsedRow = new ArrayList<>();

			Integer index = 0;
			FieldType prev = null;
			for (var elem : entry.getValue()) {

				var isCollapsed = false;
				if (!isCollapsed || !elem.equals(prev)) {
					var fields = new Fields(elem, index, 0);
					collapsedRow.add(fields);
					prev = elem;
				}

				var currentFields = collapsedRow.get(collapsedRow.size() - 1);
				currentFields.amount += 1;
				index++;

			}

			ret.put(entry.getKey(), collapsedRow);
		}

		return ret;
	}

}
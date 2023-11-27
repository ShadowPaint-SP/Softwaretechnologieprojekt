package campingplatz.plots;

import campingplatz.reservation.Reservation;
import campingplatz.reservation.ReservationCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
		FREE_COMPLETELY(0, "catalog.fields.free.completely", "submit"),
		FREE_HIGHLIGHTED(1, "catalog.fields.free.highlighted", "submit"),
		FREE_SELECTED(2, "catalog.fields.free.selected", "submit"),
		RESERVED_OTHER(3, "catalog.fields.reserved.other", "button"),
		RESERVED_SELF(4, "catalog.fields.reserved.self", "button");

		public final String clickability;
		public final Integer value;
		public final String css;
		public final String label;

		FieldType(Integer size, String arg, String clickability) {
			this.value = size;
			this.css = arg + ".css";
			this.label = arg + ".label";
			this.clickability = clickability;
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
	public PlotCatalogAvailabilityTable addReservations(Optional<UserAccount> user, List<Reservation> reservations) {
		// fill the table with reservation information
		for (var reservation : reservations) {

			// get the row, corresponding to the plotId of the current reservation
			var row = this.get(reservation.getPlot());
			if (row == null) {
				continue;
			}

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, reservation.getArrival())));
			int endIndex = (int) Math.min(length - 1, (ChronoUnit.DAYS.between(firstDay, reservation.getDeparture())));

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
	public PlotCatalogAvailabilityTable addHighlights(PlotCatalog.SiteState query, Set<Plot> reservedPlots) {
		// fill the table with selections
		for (var entry : this.entrySet()) {

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, query.getDefaultedArrival())));
			int endIndex = (int) Math.min(length - 1, (ChronoUnit.DAYS.between(firstDay, query.getDefaultedDeparture())));

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
	public PlotCatalogAvailabilityTable addSelections(ReservationCart reservationCart) {
		// fill the table with selections
		for (var reservation : reservationCart) {
			// get the row, corresponding to the plotId of the current reservation
			var row = this.get(reservation.getPlot());
			if (row == null) {
				continue;
			}

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.DAYS.between(firstDay, reservation.getArrival())));
			int endIndex = (int) Math.min(length - 1, (ChronoUnit.DAYS.between(firstDay, reservation.getDeparture())));

			for (int i = beginIndex; i <= endIndex; i++) {
				row[i] = FieldType.FREE_SELECTED;

			}
		}

		return this;
	}

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
package campingplatz.equip;

import lombok.Getter;
import lombok.Setter;
import org.salespointframework.useraccount.UserAccount;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents the mapping from a given sport item, to the 14 elements of its
 * row in the sports item view
 * <p>
 * Closely related, but diferent from
 * {@link campingplatz.plots.PlotCatalogAvailabilityTable}.
 * This is a far simpler, only containing one row
 */
public class SportItemAvailabilityTable extends ArrayList<SportItemAvailabilityTable.Field> {

	public static enum FieldType {
		FREE_COMPLETELY(0, "catalog.fields.free.completely", "submit", "bg-transparent"),
		FREE_SELECTED(1, "catalog.fields.free.selected", "submit", "bg-green-500"),
		RESERVED_OTHER(2, "catalog.fields.reserved.other", "button", "bg-red-500"),
		RESERVED_SELF(3, "catalog.fields.reserved.self", "button", "bg-yellow-600");

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

	public static class Field {
		@Getter
		@Setter
		FieldType type;

		@Getter
		@Setter
		Integer index;

		@Getter
		@Setter
		Integer amount;

		Field(FieldType type, Integer index, Integer amount) {
			this.type = type;
			this.index = index;
			this.amount = amount;
		}
	}

	// just some "global" variables stored for convenience
	LocalDateTime firstTime;
	LocalDateTime lastTime;
	Integer length;
	SportItem currentItem;

	public SportItemAvailabilityTable(LocalDateTime firstTime, LocalDateTime lastDay, SportItem currentItem) {
		this.length = (int) (ChronoUnit.HOURS.between(firstTime, lastDay));
		this.firstTime = firstTime;
		this.lastTime = lastDay;
		this.currentItem = currentItem;
	}

	public SportItemAvailabilityTable addMaxAmount(Integer n) {
		this.clear();

		for (int i = 0; i <= length; i++) {
			var prototype = new Field(FieldType.FREE_COMPLETELY, i, n);
			this.add(prototype);
		}

		return this;
	}

	public SportItemAvailabilityTable addReservations(Optional<UserAccount> user,
			List<SportItemReservation> reservations) {

		for (var reservation : reservations) {

			if (!reservation.getProduct().getId().equals(currentItem.getId())) {
				continue;
			}

			// calculate begin and end index.
			// we do this, because we need numbers relative to zero
			// for indexing into an array
			int beginIndex = (int) Math.max(0, (ChronoUnit.HOURS.between(firstTime, reservation.getBegin())));
			int endIndex = (int) Math.min((long) length - 1,
					(ChronoUnit.HOURS.between(firstTime, reservation.getEnd())));

			for (int i = beginIndex; i <= endIndex; i++) {
				var prevValue = this.get(i);

				Field newValue;
				if (user.isPresent() && reservation.getUser() == user.get()) {
					newValue = new Field(FieldType.RESERVED_SELF, prevValue.index, prevValue.amount - 1);
				} else if (prevValue.amount == 1 && prevValue.type != FieldType.RESERVED_SELF) {
					newValue = new Field(FieldType.RESERVED_OTHER, prevValue.index, prevValue.amount - 1);
				} else {
					newValue = new Field(prevValue.type, prevValue.index, prevValue.amount - 1);
				}

				this.set(i, newValue);
			}

		}

		return this;
	}

	public SportItemAvailabilityTable addSelections(SportItemCart reservationCart) {

		for (var field : reservationCart) {
			var time = field.getTime();
			var sportItem = field.getProduct();

			if (!sportItem.getId().equals(currentItem.getId())) {
				continue;
			}

			if (time.isBefore(firstTime) || time.isAfter(lastTime)) {
				continue;
			}

			int index = (int) Math.max(0, (ChronoUnit.HOURS.between(firstTime, time)));

			var prevValue = this.get(index);
			var newValue = new Field(FieldType.FREE_SELECTED, prevValue.index, prevValue.amount);
			this.set(index, newValue);
		}

		return this;
	}

}
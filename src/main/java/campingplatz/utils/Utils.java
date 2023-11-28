package campingplatz.utils;

import campingplatz.reservation.Reservable;
import campingplatz.reservation.ReservationEntry;
import org.salespointframework.catalog.Product;

public class Utils {

    // hide constructor
    private Utils() {
    }


	public static <T extends Reservable> int compareReservationEntries(ReservationEntry<T> first, ReservationEntry<T> second) {
		var firstName = first.getProduct().getName();
		var secondName = second.getProduct().getName();
		var nameComparison = firstName.compareTo(secondName);

		if (nameComparison != 0){
			return nameComparison;
		}

		var firstDate = first.getTime();
		var secondDate = second.getTime();

		return firstDate.compareTo(secondDate);
	}

}
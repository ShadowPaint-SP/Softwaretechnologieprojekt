package campingplatz.reservation;

import org.salespointframework.catalog.Product;

import java.util.List;

public interface ReservationDiscountStrategy<T extends Product, U extends Reservation<T>> {

	U discount(U reservation);

	default List<U> discountAll(List<U> reservations) {
		return reservations.stream().map(this::discount).toList();
	}

}

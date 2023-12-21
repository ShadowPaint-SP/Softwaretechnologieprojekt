package campingplatz.reservation;

import org.salespointframework.catalog.Product;

import java.util.List;

public interface ReservationDiscounter<T extends Product, U extends Reservation<T>> {

	/**
	 * sets the discount on a reservation according to some logic
	 */
	void applyDiscount(U reservation);

	default void applyDiscountToAll(List<U> reservations) {
		for (var reservation: reservations){
			applyDiscount(reservation);
		}
	}

}

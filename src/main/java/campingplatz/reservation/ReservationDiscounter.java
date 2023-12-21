package campingplatz.reservation;

import org.salespointframework.catalog.Product;

import java.util.List;

public abstract class ReservationDiscounter<T extends Product, U extends Reservation<T>> {

	/**
	 * sets the discount on a reservation according to some logic
	 */
	public abstract void applyDiscount(U reservation);

	public void applyDiscountToAll(List<U> reservations) {
		for (var reservation: reservations){
			applyDiscount(reservation);
		}
	}

}

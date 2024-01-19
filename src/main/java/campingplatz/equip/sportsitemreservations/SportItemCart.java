package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.Cart;

/**
 * Represents a shopping cart specifically designed for managing sport item reservations.
 * <p>
 * Extends the generic {@link Cart} with SportItem as the product type and SportItemReservation as the reservation type.
 * This class provides functionality for adding, removing, and checking reservations for sport items.
 */
public class SportItemCart extends Cart<SportItem, SportItemReservation> {
	public SportItemCart() {
		super(SportItemReservation.class);
	}
}

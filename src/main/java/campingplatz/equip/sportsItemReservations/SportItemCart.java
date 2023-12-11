package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.Cart;

public class SportItemCart extends Cart<SportItem, SportItemReservation> {
	public SportItemCart() {
		super(SportItemReservation.class);
	}
}

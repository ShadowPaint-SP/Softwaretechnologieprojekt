package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.Reservation;
import jakarta.persistence.Entity;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents a reservation for a sport item.
 * <p>
 * This class extends the generic Reservation class, specifying the type for the product (SportItem).
 *
 * @see Reservation
 */
@Entity
public class SportItemReservation extends Reservation<SportItem> {
	public SportItemReservation() {
		super();
	}

    /**
     * Parameterized constructor for SportItemReservation.
     *
     * @param userAccount User account associated with the reservation.
     * @param sportItem   Sport item being reserved.
     * @param arrival     Arrival time for the reservation.
     * @param departure   Departure time for the reservation.
     */
	public SportItemReservation(UserAccount userAccount,
			SportItem sportItem,
			LocalDateTime arrival,
			LocalDateTime departure) {
		super(userAccount, sportItem, arrival, departure);
	}


    /**
     * Returns the time unit used for representing the interval of the reservation.
     *
     * @return The ChronoUnit representing the time unit of the reservation interval (HOURS).
     */
	@Override
	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.HOURS;
	}

}

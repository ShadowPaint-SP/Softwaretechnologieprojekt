package campingplatz.equip;

import campingplatz.reservation.Reservation;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SportItemReservation extends Reservation<SportItem> {
	public SportItemReservation() {
		super();
	}

	public SportItemReservation(UserAccount userAccount, SportItem sportItem, LocalDateTime arrival, LocalDateTime departure) {
		super(userAccount, sportItem, arrival, departure);
	}

	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.HOURS;
	}

}

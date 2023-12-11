package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.Reservation;
import jakarta.persistence.Entity;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class SportItemReservation extends Reservation<SportItem> {
	public SportItemReservation() {
		super();
	}

	public SportItemReservation(UserAccount userAccount,
			SportItem sportItem,
			LocalDateTime arrival,
			LocalDateTime departure) {
		super(userAccount, sportItem, arrival, departure);
	}

	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.HOURS;
	}

}

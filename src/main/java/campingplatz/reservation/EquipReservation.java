package campingplatz.reservation;

import campingplatz.equip.SportItem;
import jakarta.persistence.Entity;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class EquipReservation extends Reservation<SportItem> {

	public EquipReservation() {
		super();
	}

	public EquipReservation(UserAccount userAccount, SportItem sportItem, LocalDateTime arrival, LocalDateTime departure) {
		super(userAccount, sportItem, arrival, departure);
	}

	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.DAYS;
	}
}

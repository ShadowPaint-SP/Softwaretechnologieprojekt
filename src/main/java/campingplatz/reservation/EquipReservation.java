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

	public EquipReservation(UserAccount userAccount, SportItem sportItem, LocalDateTime startTime, LocalDateTime endTime) {
		super(userAccount, sportItem, startTime, endTime);
	}

	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.HOURS;
	}
}

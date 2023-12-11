package campingplatz.plots.plotreservations;

import campingplatz.plots.Plot;
import campingplatz.reservation.Reservation;
import jakarta.persistence.Entity;
import org.salespointframework.useraccount.UserAccount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class PlotReservation extends Reservation<Plot> {

	public PlotReservation() {
		super();
	}

	public PlotReservation(UserAccount userAccount, Plot plot, LocalDateTime arrival, LocalDateTime departure) {
		super(userAccount, plot, arrival, departure);
	}

	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.DAYS;
	}
}

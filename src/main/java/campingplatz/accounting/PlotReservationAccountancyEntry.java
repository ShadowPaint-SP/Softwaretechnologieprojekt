package campingplatz.accounting;

import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.reservation.Reservation;
import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

@Entity
public class PlotReservationAccountancyEntry extends AccountancyEntry {

	String plotName;
	String userName;
	LocalDateTime begin;
	LocalDateTime end;


	public PlotReservationAccountancyEntry(PlotReservation reservation) {
		super(reservation.getPrice());

		plotName = reservation.getProduct().getName();
		userName = reservation.getUser().getUsername();
		begin = reservation.getBegin();
		end = reservation.getEnd();
	}

	public PlotReservationAccountancyEntry() {

	}

}

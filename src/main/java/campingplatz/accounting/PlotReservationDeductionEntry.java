package campingplatz.accounting;

import campingplatz.plots.plotReservations.PlotReservation;
import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

import java.time.LocalDateTime;

@Entity
public class PlotReservationDeductionEntry extends AccountancyEntry {

	String plotName;
	String userName;
	LocalDateTime begin;
	LocalDateTime end;


	public PlotReservationDeductionEntry(PlotReservation reservation) {
		super(reservation.getPrice().multiply(-1));

		plotName = reservation.getProduct().getName();
		userName = reservation.getUser().getUsername();
		begin = reservation.getBegin();
		end = reservation.getEnd();
	}

	public PlotReservationDeductionEntry() {

	}
}

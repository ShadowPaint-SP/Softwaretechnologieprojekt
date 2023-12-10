package campingplatz.accounting;

import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.reservation.Reservation;
import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;

@Entity
public class PlotReservationAccountancyEntry extends AccountancyEntry {

	private static String description(PlotReservation reservation){

		String ret = "";

		ret += "Product Name: " + reservation.getProduct().getName() + ",\n";
		ret += "Nutzer Name: " + reservation.getUser().getUsername() + ",\n";
		ret += "von: " + reservation.getBegin() + ",\n";
		ret += "bis: " + reservation.getEnd();

		return ret;
	}


	public PlotReservationAccountancyEntry(PlotReservation reservation) {
		super(reservation.getPrice(), description(reservation));
	}

	public PlotReservationAccountancyEntry() {

	}

}

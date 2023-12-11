package campingplatz.accounting;

import campingplatz.plots.plotreservations.PlotReservation;
import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class PlotReservationAccountancyEntry extends AccountancyEntry {

	private static String description(PlotReservation reservation) {

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

}

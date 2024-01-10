package campingplatz.accounting;

import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

import campingplatz.plots.plotreservations.PlotReservation;

@Entity
public class PlotReservationDeductionEntry extends AccountancyEntry {

	private static String description(PlotReservation reservation) {

		String ret = "";

		ret += "Produkt Name: " + reservation.getProduct().getName() + ",\n";
		ret += "Nutzer Name: " + reservation.getUser().getUsername() + ",\n";
		ret += "von: " + reservation.getBegin() + ",\n";
		ret += "bis: " + reservation.getEnd() + ",\n";
		ret += "rabatt: " + reservation.getDiscount();

		return ret;
	}

	public PlotReservationDeductionEntry(PlotReservation reservation) {
		super(reservation.getPrice().multiply(-1), description(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public PlotReservationDeductionEntry() {

	}
}

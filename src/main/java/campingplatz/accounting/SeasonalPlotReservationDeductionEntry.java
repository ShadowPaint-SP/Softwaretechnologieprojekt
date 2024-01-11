package campingplatz.accounting;

import jakarta.persistence.Entity;
import org.salespointframework.accountancy.AccountancyEntry;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;

@Entity
public class SeasonalPlotReservationDeductionEntry extends AccountancyEntry {

	private static String description(SeasonalPlotReservation reservation) {

		String ret = "";

		ret += "Product Name: " + reservation.getProduct().getName() + ",\n";
		ret += "Nutzer Name: " + reservation.getUser().getUsername() + ",\n";
		ret += "von: " + reservation.getBegin() + ",\n";
		ret += "bis: " + reservation.getEnd();

		return ret;
	}

	public SeasonalPlotReservationDeductionEntry(SeasonalPlotReservation reservation) {
		super(reservation.getPrice().multiply(-1), description(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SeasonalPlotReservationDeductionEntry() {

	}
}

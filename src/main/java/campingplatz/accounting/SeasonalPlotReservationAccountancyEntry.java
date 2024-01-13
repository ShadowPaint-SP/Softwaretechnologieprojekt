package campingplatz.accounting;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.utils.Utils;
import jakarta.persistence.Entity;

import java.time.format.DateTimeFormatter;

import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class SeasonalPlotReservationAccountancyEntry extends AccountancyEntry {

	private static String description(SeasonalPlotReservation reservation) {

		String ret = "";

		ret += "Product Name: " + reservation.getProduct().getName() + ",\n";
		ret += "Nutzer Name: " + reservation.getUser().getUsername() + ",\n";
		ret += "von: " + Utils.formatDate(reservation.getBegin()) + ",\n";
		ret += "bis: " + Utils.formatDate(reservation.getEnd());

		return ret;
	}

	public SeasonalPlotReservationAccountancyEntry(SeasonalPlotReservation reservation) {
		super(reservation.getPrice(), description(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SeasonalPlotReservationAccountancyEntry() {

	}

}

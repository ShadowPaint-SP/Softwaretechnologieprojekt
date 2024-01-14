package campingplatz.accounting;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.utils.Utils;
import jakarta.persistence.Entity;

import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class SeasonalPlotReservationAccountancyEntry extends AccountancyEntry {

	private static String description(SeasonalPlotReservation reservation) {

		String ret = "";
		String format = "%1$-40s,%2$-20s,%3$-17s,%4$-17s\n";

		ret += String.format(format, 
			"Product Name: ",
			"Nutzer Name: ",
			"von: ",
			"bis: "
		);

		ret += String.format(format, 
			reservation.getProduct().getName().replace("\n", " "),
			reservation.getUser().getUsername(),
			Utils.formatDate(reservation.getBegin()),
			Utils.formatDate(reservation.getEnd())
		);
		
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

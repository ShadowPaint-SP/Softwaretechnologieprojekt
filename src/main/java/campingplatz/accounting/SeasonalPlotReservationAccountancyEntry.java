package campingplatz.accounting;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.utils.Utils;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SeasonalPlotReservationAccountancyEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(SeasonalPlotReservation reservation) {

		List<String> ret = new ArrayList<>();

		ret.add(reservation.getProduct().getName().replace("\n", " "));
		ret.add(reservation.getUser().getUsername());
		ret.add(Utils.formatDate(reservation.getBegin()));
		ret.add(Utils.formatDate(reservation.getEnd()));

		return ret;
	}

	public SeasonalPlotReservationAccountancyEntry(SeasonalPlotReservation reservation) {
		super(reservation.getPrice(), descriptions(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SeasonalPlotReservationAccountancyEntry() {

	}

}

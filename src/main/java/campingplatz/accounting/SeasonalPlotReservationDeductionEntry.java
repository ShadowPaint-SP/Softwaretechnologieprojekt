package campingplatz.accounting;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.utils.Utils;

@Entity
public class SeasonalPlotReservationDeductionEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(SeasonalPlotReservation reservation) {

		List<String> ret = new ArrayList<>();

		ret.add(reservation.getProduct().getName().replace("\n", " "));
		ret.add(reservation.getUser().getUsername());
		ret.add(Utils.formatDate(reservation.getBegin()));
		ret.add(Utils.formatDate(reservation.getEnd()));

		return ret;
	}

	public SeasonalPlotReservationDeductionEntry(SeasonalPlotReservation reservation) {
		super(reservation.getPrice().multiply(-1), descriptions(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SeasonalPlotReservationDeductionEntry() {

	}
}

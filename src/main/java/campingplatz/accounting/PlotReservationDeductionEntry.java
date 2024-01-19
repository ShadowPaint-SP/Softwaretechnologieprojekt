package campingplatz.accounting;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.utils.Utils;

@Entity
public class PlotReservationDeductionEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(PlotReservation reservation) {

		List<String> ret = new ArrayList<>();

		ret.add(reservation.getProduct().getName().replace("\n", " "));
		ret.add(reservation.getUser().getUsername());
		ret.add(Utils.formatDate(reservation.getBegin()));
		ret.add(Utils.formatDate(reservation.getEnd()));

		return ret;
	}

	public PlotReservationDeductionEntry(PlotReservation reservation) {
		super(reservation.getPrice().multiply(-1), descriptions(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public PlotReservationDeductionEntry() {

	}
}

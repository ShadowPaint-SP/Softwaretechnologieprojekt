package campingplatz.accounting;

import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.utils.Utils;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SportsItemDeductionEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(SportItemReservation reservation) {

		List<String> ret = new ArrayList<>();

		ret.add(reservation.getProduct().getName().replace("\n", " "));
		ret.add(reservation.getUser().getUsername());
		ret.add(Utils.formatDate(reservation.getBegin()));
		ret.add(Utils.formatDate(reservation.getEnd()));

		return ret;
	}

	public SportsItemDeductionEntry(SportItemReservation reservation) {
		super(reservation.getPrice().multiply(-1), descriptions(reservation));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SportsItemDeductionEntry() {

	}

}

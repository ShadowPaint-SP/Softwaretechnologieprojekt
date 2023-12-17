package campingplatz.plots.plotdiscounts;

import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.reservation.ReservationDiscountStrategy;
import campingplatz.utils.Utils;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class PlotReservationDiscountStrategy implements ReservationDiscountStrategy<Plot, PlotReservation> {

	List<PlotReservation> reservations;

	PlotReservationDiscountStrategy(List<PlotReservation> reservations){
		this.reservations = reservations;
	}

	public PlotReservation discount(PlotReservation reservation){

		var totalOverlappingLength = 0d;
		for (var otherReservation : reservations){

			var overlapArrival = Utils.max(reservation.getBegin(), otherReservation.getBegin());
			var overlapDeparture = Utils.min(reservation.getEnd(), otherReservation.getEnd());

			if (overlapArrival.isAfter(overlapDeparture)){
				continue;
			}

			totalOverlappingLength += ChronoUnit.DAYS.between(overlapArrival, overlapDeparture) + 1;

		}

		var normalizedOverlapping = totalOverlappingLength / reservation.duration();


		// TODO: contiune with the lookup...


		return null;

	}

}

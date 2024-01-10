package campingplatz.plots.plotdiscounts;

import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.reservation.ReservationDiscounter;
import campingplatz.utils.Utils;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.round;


public class PlotReservationDiscounter extends ReservationDiscounter<Plot, PlotReservation> {

	List<PlotReservation> reservations;
	List<PlotReservationDiscount> discounts;

	public PlotReservationDiscounter(List<PlotReservation> reservations, List<PlotReservationDiscount> discounts){
		this.reservations = reservations;
		this.discounts = discounts;
	}

	@Override
	public void applyDiscount(PlotReservation reservation){

		var totalOverlappingLength = 0d;
		for (var otherReservation : reservations){

			var overlapArrival = Utils.max(reservation.getBegin(), otherReservation.getBegin());
			var overlapDeparture = Utils.min(reservation.getEnd(), otherReservation.getEnd());

			if (overlapArrival.isAfter(overlapDeparture)){
				continue;
			}

			totalOverlappingLength += ChronoUnit.DAYS.between(overlapArrival, overlapDeparture) + 1;

		}

		var normalizedOverlappingAmount = totalOverlappingLength / reservation.duration();
		var roundedOverlappingAmount = round(normalizedOverlappingAmount);


		PlotReservationDiscount highestFittingDiscount = null;
		for (var discount : discounts){
			if (roundedOverlappingAmount >= discount.getAmount()){
				highestFittingDiscount = discount;
			}
		}

		 if (highestFittingDiscount != null){
			 reservation.setDiscount(highestFittingDiscount.getDiscount());
		 }

	}

}

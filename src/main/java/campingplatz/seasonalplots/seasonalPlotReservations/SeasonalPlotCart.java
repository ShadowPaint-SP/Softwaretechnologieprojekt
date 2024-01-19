package campingplatz.seasonalplots.seasonalPlotReservations;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.useraccount.UserAccount;

import campingplatz.reservation.Cart;
import campingplatz.seasonalplots.SeasonalPlot;

/**
 * All permanent camper plots that are to be reserved
 * must first be placed in the shopping cart.
 * Therefore, SeasonalPlotCart is an extension of {@link Cart}.
 */
public class SeasonalPlotCart extends Cart<SeasonalPlot, SeasonalPlotReservation> {

	List<SeasonalPlotReservation> reservations = new ArrayList<>();
	
	public SeasonalPlotCart() {
		super(SeasonalPlotReservation.class);
	}

	
	@Override
	public boolean add(SeasonalPlotReservation reservation) {
		super.add(reservation);

		this.reservations.add(reservation);

		return true;
	}

	@Override
	public List<SeasonalPlotReservation> getReservationsOfUser(UserAccount user) {
		return reservations;
	}

	@Override
	public void clear(){
		super.clear();
		reservations.clear();
	}

	@Override
	public boolean remove(SeasonalPlotReservation reservation) {
		
		reservations.remove(reservation);
		return super.remove(reservation);
	}

}

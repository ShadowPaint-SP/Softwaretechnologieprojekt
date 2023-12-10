package campingplatz.plots.plotReservations;

import campingplatz.plots.Plot;
import campingplatz.reservation.Cart;

public class PlotCart extends Cart<Plot, PlotReservation> {
	public PlotCart() {
		super(PlotReservation.class);
	}
}

package campingplatz.plots.plotreservations;

import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.*;
import campingplatz.reservation.Cart;

public class PlotCart extends Cart<Plot, PlotReservation> {
	public PlotCart() {
		super(PlotReservation.class);
	}
}

package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.reservation.Cart;
import campingplatz.seasonalplots.SeasonalPlot;

public class SeasonalPlotCart extends Cart<SeasonalPlot, SeasonalPlotReservation> {
	public SeasonalPlotCart() {
		super(SeasonalPlotReservation.class);
	}
}

package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.reservation.ReservationRepository;
import campingplatz.seasonalplots.SeasonalPlot;

/**
 * All reservations are saved here and
 * it inherits from {@link ReservationRepository}
 */
public interface SeasonalPlotReservationRepository
		extends ReservationRepository<SeasonalPlot, SeasonalPlotReservation> {
}

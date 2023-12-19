package campingplatz.plots.plotdiscounts;


import campingplatz.equip.SportItem;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.reservation.ReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlotReservationDiscountRepository extends CrudRepository<PlotReservationDiscount, UUID> {

	@Override
	List<PlotReservationDiscount> findAll();

}

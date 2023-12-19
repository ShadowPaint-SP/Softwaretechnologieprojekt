package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.ReservationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportItemReservationRepository extends ReservationRepository<SportItem, SportItemReservation> {
}

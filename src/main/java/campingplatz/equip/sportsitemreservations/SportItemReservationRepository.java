package campingplatz.equip.sportsitemreservations;

import campingplatz.equip.SportItem;
import campingplatz.reservation.ReservationRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing sport item reservations.
 * <p>
 * This interface extends the generic {@link ReservationRepository} for sport items.
 * It provides methods for accessing and manipulating reservations specific to sport items.
 *
 * @see ReservationRepository
 */
@Repository
public interface SportItemReservationRepository extends ReservationRepository<SportItem, SportItemReservation> {
}

package campingplatz.reservation;

import jakarta.transaction.Transactional;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoRepositoryBean
public interface ReservationRepository<T extends Product, U extends Reservation<T>> extends CrudRepository<U, UUID> {

    /**
     * Redeclare this function to make it apparent it exists
     * Implementation auto generated by Java Spring
     */
    @Override
    List<U> findAll();

    /**
     * Returns a List of Reservations with a given UserId.
     * Implementation auto generated by Java Spring
     */
    List<U> findByUserId(UserAccount.UserAccountIdentifier id);


    @Query("""
            select distinct r.product from #{#entityName} r
            where r.state = 0 or r.state = 1
            """)
    Set<T> findPlotsAll();

    /**
     * Database query to return a List of reservations, that are
     * wholy or partially reserved in the given time interval
     *
     */
    @Query("""
                select r from #{#entityName} r
                where (r.begin >= :arrival and r.begin <= :departure)
                or (r.end >= :arrival and r.end <= :departure)
                or (r.begin <= :arrival and r.end >= :departure)
            """)
    List<U> findReservationsBetween(LocalDateTime arrival, LocalDateTime departure);

    /**
     * Database query to return a Stream of productIds corresponding to plots, that
     * are wholy or partially reserved in the given time interval
     *
     */
    @Query("""
                select distinct r.product from #{#entityName} r
                where (r.begin > :arrival and r.begin <= :departure)
                or (r.end > :arrival and r.end <= :departure)
                or (r.begin <= :arrival and r.end >= :departure)
            """)
    Set<T> findProductsReservedBetween(LocalDateTime arrival, LocalDateTime departure);

    default Boolean productIsReservedIn(T product, LocalDateTime arrival, LocalDateTime departure) {
        return findProductsReservedBetween(arrival, departure).contains(product);
    }


	@Query("""
                select distinct r.user from #{#entityName} r
                where (r.product = :product)
                and (r.state != 0)
            """)
	Set<UserAccount> findUsersOfProduct(T product);


    @Modifying
    @Transactional
    @Query("""
                delete #{#entityName} r
                where r.state = 0 and r.begin < :time
            """)
    void deleteBeforeThan(LocalDateTime time);

}
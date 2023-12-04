package campingplatz.reservation;

import campingplatz.utils.Priced;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.money.MonetaryAmount;

import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The Base class of all Reservations.
 * Create more Reservations by inheriting from this class
 * and specifying the Type of the reservation. Keep in mind,
 * that this is done, because a JPA Entity cannot be generic
 *
 */
@Entity
@EqualsAndHashCode
public class Reservation<T extends Product> implements Priced {

    @Getter
    @Id
    public UUID id;

    @Getter
    @Setter
    @ManyToOne
    private UserAccount user;

    @Getter
    @Setter
    @ManyToOne
    private T product;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime begin;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end;

    public Reservation() {
        this.id = UUID.randomUUID();
    }

    public Reservation(UserAccount user, T product, LocalDateTime begin, LocalDateTime end) {

        this.id = UUID.randomUUID();

        this.user = user;
        this.product = product;

        this.begin = begin;
        this.end = end;
    }

    @Override
    public MonetaryAmount getPrice() {
        var price = product.getPrice();
        return price.multiply(duration());
    }

    // meant to be overridden
    public ChronoUnit getIntervalUnit() {
        return null;
    }

    /**
     * Get the duration between begin and end. The unit of the duration
     * is determined by the getern value of {@link Reservable.getIntervalUnit}
     */
    public long duration() {
        var units = getIntervalUnit();
        return units.between(begin, end);
    }

}

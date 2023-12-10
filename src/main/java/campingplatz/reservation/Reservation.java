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
 * <p>
 * Create more Reservations by inheriting from this class
 * and specifying the Type of the reservation. Keep in mind,
 * that this is done, because a JPA Entity cannot be generic
 * Because of that, you should not add any extensions to this
 * class, the ReservationRepository will just return a regular
 * Reservation anyway
 *
 */
@MappedSuperclass
@EqualsAndHashCode
public abstract class Reservation<T extends Product> implements Priced {

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

    @Getter
    @Setter
    private State state;

    public Reservation() {
        this.id = UUID.randomUUID();
        this.state = State.NOT_TAKEN;
    }

    public Reservation(UserAccount user, T product, LocalDateTime begin, LocalDateTime end) {

        this.id = UUID.randomUUID();
        this.state = State.NOT_TAKEN;

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
    public abstract ChronoUnit getIntervalUnit();

    /**
     * Get the duration between begin and end. The unit of the duration
     * is determined by the getern value of {@link Reservable.getIntervalUnit}
     */
    public long duration() {
        var units = getIntervalUnit();
        return units.between(begin, end);
    }

    public static enum State {
        NOT_TAKEN(0),
        TAKEN(1);

        private Integer value;

        State(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static State fromNumber(Integer i) {
            return switch (i) {
                case 0 -> NOT_TAKEN;
                case 1 -> TAKEN;
                default -> null;
            };
        }
    }

}

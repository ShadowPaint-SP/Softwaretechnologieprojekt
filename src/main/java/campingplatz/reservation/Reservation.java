package campingplatz.reservation;

import campingplatz.utils.Priced;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * The Base class of all Reservations.
 * <p>
 * Create specific Reservations by inheriting from this class
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

	@Id
    @Getter
    public UUID id;

	@Getter
	private double discount;

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
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:MM")
    private LocalDateTime begin;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:MM")
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
		this.discount = 0d;

        this.user = user;
        this.product = product;

        this.begin = begin;
        this.end = end;
    }

	public Reservation(Reservation<T> original) {

		this.id = UUID.randomUUID();
		this.state = original.state;
		this.discount = original.discount;

		this.user = original.user;
		this.product = original.product;

		this.begin = original.begin;
		this.end = original.end;
	}

	public void setDiscount(double discount){
		if (0 <= discount && discount <= 1){
			this.discount = discount;
		}
		else {
			throw new IllegalArgumentException("discount has to be between 0 and 1, but was " + discount);
		}

	}


    @Override
    public MonetaryAmount getPreDiscountPrice() {
        var price = product.getPrice();
        return price.multiply(duration());
    }

	@Override
	public MonetaryAmount getPrice() {
		var price = product.getPrice();
		return price.multiply(duration()).multiply(1 - discount);
	}

	@Override
	public Boolean hasDiscount(){
		return discount != 0;
	}


    // meant to be overridden
    public abstract ChronoUnit getIntervalUnit();

    /**
     * Get the duration between begin and end. The unit of the duration
     * is determined by the getern value of {@link Reservable.getIntervalUnit}
     */
    public long duration() {
        var units = getIntervalUnit();
        return units.between(begin, end) + 1;
    }

    public static enum State {
        NOT_TAKEN(0),
        TAKEN(1),
		PAYED(2);

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
				case 2 -> PAYED;
                default -> null;
            };
        }
    }

}

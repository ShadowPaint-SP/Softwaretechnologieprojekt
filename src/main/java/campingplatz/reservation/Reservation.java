package campingplatz.reservation;

import campingplatz.utils.Priced;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.money.MonetaryAmount;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@EqualsAndHashCode
public class Reservation<T extends Reservable> implements Priced {

    @Getter
    private @Id UUID id;


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

    }

    public Reservation(UserAccount user, T product, LocalDateTime begin, LocalDateTime end) {

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

    /**
	 * Get the duration between begin and end. The unit of the duration
	 * is determined by the getern value of {@link Reservable.getIntervalUnit}
	 * */
	public long duration() {
        var units =  T.getIntervalUnit();
		return units.between(begin, end);
    }




    /**
     * return true, if this reservation is overlapping with the given reservation
	 * might remove it.
     */
    public boolean intersects(Reservation with) {

        if (!this.getProduct().getId().equals(with.getProduct().getId())) {
            return false;
        }

        var firstArrival = this.getBegin();
        var secondArrival = with.getBegin();
        var firstDeparture = this.getEnd();
        var secondDeparture = with.getEnd();

        var a = !firstArrival.isBefore(secondArrival) && !firstArrival.isAfter(secondDeparture);
        var b = !firstDeparture.isBefore(secondArrival) && !firstDeparture.isAfter(secondDeparture);
        var c = firstArrival.isBefore(secondArrival) && firstDeparture.isAfter(secondDeparture);
        return a || b || c;
    }
}

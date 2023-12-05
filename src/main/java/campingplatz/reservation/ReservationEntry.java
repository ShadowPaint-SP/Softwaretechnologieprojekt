package campingplatz.reservation;

import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode
public class ReservationEntry<T extends Product> implements Comparable<ReservationEntry<T>> {

    @ManyToOne
    @Getter
    @Setter
    private T product;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime time;

    public ReservationEntry() {

    }

    public ReservationEntry(T product, LocalDateTime time) {
        this.product = product;
        this.time = time;
    }

    public int compareTo(ReservationEntry<T> second) {
        var firstName = this.getProduct().getName();
        var secondName = second.getProduct().getName();
        var nameComparison = firstName.compareTo(secondName);

        if (nameComparison != 0) {
            return nameComparison;
        }

        var firstDate = this.getTime();
        var secondDate = second.getTime();

        return firstDate.compareTo(secondDate);
    }

}

package campingplatz.reservation;

import campingplatz.utils.Priced;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.UUID;


@EqualsAndHashCode
public class ReservationEntry<T extends Product> {

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
}

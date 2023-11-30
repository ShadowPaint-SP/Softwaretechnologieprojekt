package campingplatz.reservation;


import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;

import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;



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

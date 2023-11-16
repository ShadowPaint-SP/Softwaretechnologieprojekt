package campingplatz.reservation;

import campingplatz.utils.Priced;
import campingplatz.plots.Plot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
public class Reservation implements Priced {

    @Getter
    private @Id UUID id;

    @ManyToOne
    @Getter
    @Setter
    private UserAccount user;

    @ManyToOne
    @Getter
    @Setter
    private Plot plot;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrival;

    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departure;

    public Reservation() {

    }

    public Reservation(UserAccount user, Plot plot, LocalDate arrival, LocalDate departure) {

        this.id = UUID.randomUUID();

        this.user = user;
        this.plot = plot;

        this.arrival = arrival;
        this.departure = departure; // set this.days
    }

    @Override
    public MonetaryAmount getPrice() {
        var days = getDays();
        var price = plot.getPrice();

        return price.multiply(days);
    }

    public long getDays() {
        return ChronoUnit.DAYS.between(this.arrival, this.departure);
    }
}

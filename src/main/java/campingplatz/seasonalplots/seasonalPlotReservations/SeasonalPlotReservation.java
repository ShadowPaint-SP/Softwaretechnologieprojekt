package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.SeasonalPlot;
import jakarta.persistence.Entity;

import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class SeasonalPlotReservation extends Reservation<SeasonalPlot> {

	private PayMethod payMethod;

	public SeasonalPlotReservation() {
		super();
	}

	public SeasonalPlotReservation(UserAccount user, SeasonalPlot product, LocalDateTime begin, LocalDateTime end,
			PayMethod payMethod) {
		super(user, product, begin, end);
		this.payMethod = payMethod;
	}

	@Override
	public ChronoUnit getIntervalUnit() {
		return ChronoUnit.MONTHS;
	}

	public MonetaryAmount getTotalPrice(double electricity, double water) {
		if (PayMethod.MONTHLY.equals(payMethod)) {
			return getProduct().getPrice()
					.add(getProduct().settlementElectricity(electricity).add(getProduct().settlementWater(water)));
		}
		return getPrice().add(getProduct().settlementElectricity(electricity).add(getProduct().settlementWater(water)));
	}

	public enum PayMethod {
		MONTHLY, YEARLY;
	}
}

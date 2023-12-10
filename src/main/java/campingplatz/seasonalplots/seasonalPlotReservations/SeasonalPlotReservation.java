package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.plots.Plot;
import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.SeasonalPlot;
import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class SeasonalPlotReservation extends Reservation<SeasonalPlot> {

	private PayMethod payMethod;

	@Getter
	@Setter
	private boolean nextYear;

	public SeasonalPlotReservation() {
		super();
	}

	public SeasonalPlotReservation(UserAccount user, SeasonalPlot product, LocalDateTime begin, LocalDateTime end,
			PayMethod payMethod) {
		super(user, product, begin, end);
		this.payMethod = payMethod;
		this.nextYear = false;
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

	public boolean isNextYearAvaible() {
		if(LocalDateTime.now().isAfter(getEnd())){
			return true;
		}
		return false;
	}
	public enum PayMethod {
		MONTHLY, YEARLY;

		public static PayMethod fromNumberPayMethod(Integer i) {
			return switch (i) {
				case 0 -> MONTHLY;
				case 1 -> YEARLY;
				default -> MONTHLY;
			};
		}
	}
}

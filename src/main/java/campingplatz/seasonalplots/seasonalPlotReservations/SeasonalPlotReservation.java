package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.Config;
import campingplatz.seasonalplots.SeasonalPlot;
import jakarta.persistence.Entity;
import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
public class SeasonalPlotReservation extends Reservation<SeasonalPlot> {

	private PayMethod payMethod = PayMethod.YEARLY;

	private Double electricityDifference = 0.0;
	private Double waterDifference = 0.0;

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

	@Override
	public MonetaryAmount getPrice() {
		return super.getPrice()
			.add(Config.getElectricityCosts().multiply(electricityDifference))
			.add(Config.getWaterCosts().multiply(waterDifference));
	}

	public boolean isNextYearAvaible(LocalDateTime date) {
		return date.isAfter(getEnd());
	}

	public enum PayMethod {
		MONTHLY, YEARLY;

		public static PayMethod fromNumberPayMethod(Integer i) {
			return switch (i) {
				case 0 -> MONTHLY;
				case 1 -> YEARLY;
				default -> YEARLY;
			};
		}
	}

	public void setElectricityDifference(Double newElectricityMeter) {
		this.electricityDifference = getProduct().settlementElectricity(newElectricityMeter);
	}

	public void setWaterDifference(Double newWaterMeter) {
		this.waterDifference = getProduct().settlementWater(newWaterMeter);
	}
}

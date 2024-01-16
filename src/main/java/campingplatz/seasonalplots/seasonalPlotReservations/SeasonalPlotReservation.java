package campingplatz.seasonalplots.seasonalPlotReservations;

import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.Config;
import campingplatz.seasonalplots.SeasonalPlot;
import jakarta.persistence.Entity;
import lombok.Getter;

import org.salespointframework.useraccount.UserAccount;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Entity
public class SeasonalPlotReservation extends Reservation<SeasonalPlot> {


	@Getter
	private int payed_months;
	// we need to wich moth is payed for payMethod.MONTHLY

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
		if (payMethod.equals(PayMethod.MONTHLY)) {
			var price = getProduct().getPrice()
					.add(Config.getElectricityCosts().multiply(electricityDifference))
					.add(Config.getWaterCosts().multiply(waterDifference));
			return price;
		}
		return super.getPrice()
				.add(Config.getElectricityCosts().multiply(electricityDifference))
				.add(Config.getWaterCosts().multiply(waterDifference));
	}

	public boolean isNextYearAvaible(LocalDateTime date) {
		return date.isAfter(getEnd());
	}

	public enum PayMethod {

		MONTHLY(0, "monthly"),
		YEARLY(1, "yearly");

		public final Integer index;
		public final String label;

		PayMethod(Integer index, String label) {
			this.index = index;
			this.label = label;
		}

		public static PayMethod fromNumberPayMethod(Integer i) {
			return switch (i) {
				case 0 -> MONTHLY;
				case 1 -> YEARLY;
				default -> YEARLY;
			};
		}
	}

	public void setElectricityDifference(Double newElectricityMeter) {
		this.electricityDifference = this.electricityDifference
				+ getProduct().settlementElectricity(newElectricityMeter, electricityDifference);
	}

	public void setWaterDifference(Double newWaterMeter) {
		this.waterDifference = this.waterDifference + getProduct().settlementWater(newWaterMeter, waterDifference);
	}

	void updateMonthlyPayment(LocalDateTime date) {
		/* reset PAYED status if month has passed and PayMethod is MONTHLY */
		if (date.isAfter(this.getBegin().plusMonths(payed_months)) && getState() == State.PAYED
				&& payMethod == PayMethod.MONTHLY && payed_months < duration()) {
			this.setState(State.TAKEN);
			this.payed_months += 1;
		}
	}

}

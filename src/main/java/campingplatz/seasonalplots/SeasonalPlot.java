package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

@Entity
public class SeasonalPlot extends Plot {

	@Getter
	@Setter
	private double electricityMeter;

	@Getter
	@Setter
	private double waterMeter;

	public SeasonalPlot(String name, Double size, Money price, ParkingLot parking, double electricityMeter,
			double waterMeter, String imagePath, String description) { // Price is per Month

		super(name + "\n[Dauercamper]", size, price, parking, imagePath, description);

		this.electricityMeter = electricityMeter;
		this.waterMeter = waterMeter;
	}

	public SeasonalPlot() {
	}

	public MonetaryAmount settlementElectricity(double electricityMeter) { // returns the electricity costs for this
																			// time period
		double electricity = electricityMeter - this.electricityMeter;
		this.electricityMeter = electricityMeter;
		return Config.getElectricityCosts().multiply(electricity);
	}

	public MonetaryAmount settlementWater(double waterMeter) { // returns the water costs for this time period
		double water = waterMeter - this.waterMeter;
		this.waterMeter = waterMeter;
		return Config.getWaterCosts().multiply(water);
	}

	public LocalDateTime getArrival() {
		// at the first day of next month
		LocalDateTime arrival = LocalDateTime.now().plusMonths(1).withDayOfMonth(1);

		// in April next year if the season is over
		if (arrival.getMonthValue() > 10) {
			arrival = arrival.plusYears(1).withMonth(4);
		}
		// in April if the season hasn't started jet
		else if (arrival.getMonthValue() < 4) {
			arrival = arrival.withMonth(4);
		}

		return arrival;
	}
	public String getArrivalStr() {
		return getArrival().toString().substring(0, 10);
	}
	


	public LocalDateTime getDeparture() {
		return getArrival().withMonth(10).withDayOfMonth(31);
	}
	public String getDepartureStr() {
		return getDeparture().toString().substring(0, 10);
	}

}

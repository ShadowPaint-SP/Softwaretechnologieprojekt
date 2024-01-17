package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

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

	public Double settlementElectricity(double electricityMeter, Double difference) { // returns the electricity
																																										// difference for this
		// time period
		double electricity = electricityMeter - this.electricityMeter;
		if (electricity + difference < 0.0) {
			return 0.0;
		}
		this.electricityMeter = electricityMeter;
		return electricity;
	}

	public Double settlementWater(double waterMeter, Double difference) { // returns the water differnce for this time
																																				// period
		double water = waterMeter - this.waterMeter;
		if (water + difference < 0.0) {
			return 0.0;
		}
		this.waterMeter = waterMeter;
		return water;
	}

	public static LocalDateTime getArrival(LocalDateTime now) {
		// at the first day of next month
		LocalDateTime arrival = now.plusMonths(1).withDayOfMonth(1);

		if (arrival.getMonthValue() > 10) { // in April next year if the season is over
			arrival = arrival.plusYears(1).withMonth(4);
		} else if (arrival.getMonthValue() < 4) { // in April if the season hasn't started jet
			arrival = arrival.withMonth(4);
		}

		return arrival;
	}

	public String getArrivalStr(LocalDateTime now) {
		return getArrival(now).toString().substring(0, 10);
	}

	public LocalDateTime getDeparture(LocalDateTime now) {
		return getArrival(now).withMonth(10).withDayOfMonth(31);
	}

	public String getDepartureStr(LocalDateTime now) {
		return getDeparture(now).toString().substring(0, 10);
	}

}

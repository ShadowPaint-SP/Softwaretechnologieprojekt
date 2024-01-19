package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

/**
 * An extension of {@link Plot}
 * to realize permanent campsites.
 */
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

    /**
     * Returns the difference between the old and the new counter value.
     *
     * @param electricityMeter new meter count
     * @param difference       last difference from reservation
     * @return                 new difference or 0, when the new meter
     *                         under the count before the reservation
     */
	public Double settlementElectricity(double electricityMeter, Double difference) { //set new electricity meter
		double electricity = electricityMeter - this.electricityMeter;
		if (electricity + difference < 0.0) {
			return 0.0;
		}
		this.electricityMeter = electricityMeter;
		return electricity;
	}

    /**
     * Returns the difference between the old and the new counter value.
     *
     * @param waterMeter    new meter count
     * @param difference    last difference from reservation
     * @return              new difference or 0, when the new meter
     *                      under the count before the reservation
     */
	public Double settlementWater(double waterMeter, Double difference) { //set new water meter
		double water = waterMeter - this.waterMeter;
		if (water + difference < 0.0) {
			return 0.0;
		}
		this.waterMeter = waterMeter;
		return water;
	}

    /**
     *Sets the fixed arrival date to 4.1. or to the next possible period.
     *
     * @param now   present time
     * @return      next possible arrival date
     */
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

    /**
     *Sets the departure to 31.10. if the arrival has been set.
     *
     * @param now   present time
     * @return      departure date
     */
	public LocalDateTime getDeparture(LocalDateTime now) {
		return getArrival(now).withMonth(10).withDayOfMonth(31);
	}
}

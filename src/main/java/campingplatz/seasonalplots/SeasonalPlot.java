package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

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
						double waterMeter, String imagePath, String description) {	//Price is per Month

		super(name + "\n[Dauercamper]", size, price, parking, imagePath, description);

		this.electricityMeter = electricityMeter;
		this.waterMeter = waterMeter;
	}

	public SeasonalPlot() {
	}

	public MonetaryAmount settlementElectricity(double electricityMeter) { // returns the electricity costs for this time period
		double electricity = electricityMeter - this.electricityMeter;
		this.electricityMeter = electricityMeter;
		return Config.getElectricityCosts().multiply(electricity);
	}

	public MonetaryAmount settlementWater(double waterMeter) { // returns the water costs for this time period
		double water = waterMeter - this.waterMeter;
		this.waterMeter = waterMeter;
		return Config.getWaterCosts().multiply(water);
	}
}

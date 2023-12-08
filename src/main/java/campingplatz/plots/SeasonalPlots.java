package campingplatz.plots;

import campingplatz.customer.Customer;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Entity
public class SeasonalPlots extends Plot {

	@Getter
	private double electricityMeter;

	@Getter
	private double waterMeter;

	@Getter
	@Setter
	private PaymentMethod paymentMethod;

	public SeasonalPlots(String name, Double size, Money price, ParkingLot parking, double electricityMeter,
			double waterMeter, PaymentMethod paymentMethod) {
		//loliger quick fix pls make pictures great again
		super(name, size, price, parking,
			"/img/placeholder.png",
			"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam");

		this.electricityMeter = electricityMeter;
		this.waterMeter = waterMeter;
		this.paymentMethod = paymentMethod;
	}

	public SeasonalPlots() {
	}

	public Money settlementElectricity(double electricityMeter) { // returns the electricity costs for this time period
		double electricity = electricityMeter - this.electricityMeter;
		this.electricityMeter = electricityMeter;
		return Config.getElectricityCosts().multiply(electricity);
	}

	public Money settlementWater(double waterMeter) { // returns the water costs for this time period
		double water = waterMeter - this.waterMeter;
		this.waterMeter = waterMeter;
		return Config.getWaterCosts().multiply(water);
	}

	public enum PaymentMethod {
		MONTHLY, SEASONAL
	}

}

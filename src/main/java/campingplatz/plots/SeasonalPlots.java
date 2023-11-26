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

	@Getter @Setter
	private Customer permanentCamper;

	@Getter @Setter
	private PaymentMethod paymentMethod;

	public SeasonalPlots(String name, Double size, Money price, ParkingLot parking, double electricityMeter, double waterMeter, Customer permanentCamper, PaymentMethod paymentMethod) {
		super(name, size, price, parking);
		this.electricityMeter = electricityMeter;
		this.waterMeter = waterMeter;
		this.permanentCamper = permanentCamper;
		this.paymentMethod = paymentMethod;
	}

	public SeasonalPlots() {
	}

	public double setElectricityMeter(double electricityMeter) { //returns the electricity costs for this time period
		double electricity = electricityMeter - this.electricityMeter;
		this.electricityMeter = electricityMeter;
		return Math.ceil(electricity*Variables.getElectricityCosts()*100)/100;
	}

	public double setWaterMeter(double waterMeter) {  //returns the water costs for this time period
		double water = waterMeter - this.waterMeter;
		this.waterMeter = waterMeter;
		return Math.ceil(water*Variables.getWaterCosts()*100)/100;
	}

	public enum PaymentMethod {
		NONE, MONTHLY, SEASONAL
	}

}

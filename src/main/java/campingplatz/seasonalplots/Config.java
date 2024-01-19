package campingplatz.seasonalplots;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.EURO;

/**
 * The class contains the variables
 * for the water and electricity costs
 */
public class Config {
	private static MonetaryAmount electricityCosts = Money.of(0.29, EURO); // per kWh in Euro
	private static MonetaryAmount waterCosts = Money.of(2.59,EURO); // per m^3 in Euro

	public static MonetaryAmount getElectricityCosts() {
		return electricityCosts;
	}

	public static void setElectricityCosts(MonetaryAmount electricityCosts) {
		Config.electricityCosts = electricityCosts;
	}

	public static void setWaterCosts(MonetaryAmount waterCosts) {
		Config.waterCosts = waterCosts;
	}

	public static MonetaryAmount getWaterCosts() {
		return waterCosts;
	}
}

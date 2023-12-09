package campingplatz.seasonalplots;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;

public class Config {
	private static Money electricityCosts = Money.of(0.29, EURO); // per kWh in Euro
	private static Money waterCosts = Money.of(2.59,EURO); // per m^3 in Euro

	public static Money getElectricityCosts() {
		return electricityCosts;
	}

	public static void setElectricityCosts(Money electricityCosts) {
		Config.electricityCosts = electricityCosts;
	}

	public static void setWaterCosts(Money waterCosts) {
		Config.waterCosts = waterCosts;
	}

	public static Money getWaterCosts() {
		return waterCosts;
	}
}

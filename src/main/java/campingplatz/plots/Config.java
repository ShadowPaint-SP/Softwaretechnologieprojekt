package campingplatz.plots;

public class Config { //TODO Strom Wasser für normale Plots
	private static double electricityCosts = 0.29; //per kWh in Euro
	private static double waterCosts = 2.59; //per m^3 in Euro

	public static double getElectricityCosts() {
		return electricityCosts;
	}

	public static void setElectricityCosts(float electricityCosts) {
		Config.electricityCosts = electricityCosts;
	}

	public static double getWaterCosts() {
		return waterCosts;
	}

	public static void setWaterCosts(int waterCosts) {
		Config.waterCosts = waterCosts;
	}
}

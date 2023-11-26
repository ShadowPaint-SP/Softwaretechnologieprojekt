package campingplatz.plots;

public class Variables {
	private static double electricityCosts = 0.29; //per kWh in Euro
	private static double waterCosts = 2.59; //per m^3 in Euro

	public static double getElectricityCosts() {
		return electricityCosts;
	}

	public static void setElectricityCosts(float electricityCosts) {
		Variables.electricityCosts = electricityCosts;
	}

	public static double getWaterCosts() {
		return waterCosts;
	}

	public static void setWaterCosts(int waterCosts) {
		Variables.waterCosts = waterCosts;
	}
}

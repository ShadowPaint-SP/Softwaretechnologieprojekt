package campingplatz.plots;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {

	@Test
	public void testGetElectricityCosts() {
		Config.setElectricityCosts(0.29f);
		assertEquals(0.29f, Config.getElectricityCosts());
	}

	@Test
	public void testSetElectricityCosts() {
		Config.setElectricityCosts(0.30f);
		assertEquals(0.30f, Config.getElectricityCosts());
	}

	@Test
	public void testGetWaterCosts() {
		assertEquals(2.59, Config.getWaterCosts());
	}

	@Test
	public void testSetWaterCosts() {
		Config.setWaterCosts(3);
		assertEquals(3, Config.getWaterCosts());
	}

}

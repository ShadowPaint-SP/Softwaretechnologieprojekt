package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class ConfigTest {

	@Test
	public void testGetElectricityCosts() {
		Config.setElectricityCosts(Money.of(0.29, EURO));
		assertEquals(Money.of(0.29, EURO), Config.getElectricityCosts());
	}

	@Test
	public void testSetElectricityCosts() {
		Config.setElectricityCosts(Money.of(0.30, EURO));
		assertEquals(Money.of(0.30, EURO), Config.getElectricityCosts());
	}

	@Test
	public void testGetWaterCosts() {
		assertEquals(Money.of(2.59, EURO), Config.getWaterCosts());
	}

	@Test
	public void testSetWaterCosts() {
		Config.setWaterCosts(Money.of(3, EURO));
		assertEquals(Money.of(3, EURO), Config.getWaterCosts());
	}

}

package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.salespointframework.core.Currencies.EURO;

import static org.junit.jupiter.api.Assertions.*;

class SportItemTest {

	private SportItem defaultItem;

	@BeforeEach
	void setUp() {
		this.defaultItem = new SportItem("Default",
				Money.of(10, EURO),
				Money.of(10, EURO),
				"Default",
				0,
				"Default",
				"Default");
	}

	@Test
	public void SportItemConstructorTest() {
		assertDoesNotThrow(() -> {
			new SportItem("Default",
					Money.of(10, EURO),
					Money.of(10, EURO),
					"Default",
					0,
					"Default",
					"Default");
		}, "SportItem constructor failed");
	}

	@Test
	public void SportItemSetGetDepositTest() {
		Money deposit = Money.of(100000, EURO);

		this.defaultItem.setDeposit(deposit);

		assertEquals(deposit, this.defaultItem.getDeposit());
	}

	@Test
	public void SportItemSetGetAmountTest() {
		int amount = 100000;

		this.defaultItem.setAmount(amount);

		assertEquals(amount, this.defaultItem.getAmount());
	}

	@Test
	public void SportItemSetGetDescTest() {
		String desc = "100000";

		this.defaultItem.setDesc(desc);

		assertEquals(desc, this.defaultItem.getDesc());
	}

	@Test
	public void SportItemSetGetImagePathTest() {
		String imagePath = "100000";

		this.defaultItem.setImagePath(imagePath);

		assertEquals(imagePath, this.defaultItem.getImagePath());
	}

}
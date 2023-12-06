package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.salespointframework.core.Currencies.EURO;

import static org.junit.jupiter.api.Assertions.*;

class SportItemTest {

	@BeforeEach
	void setUp() {

	}

	@Test
	@DisplayName("Erstellt SportItem Objekte korrekt")
	public void SportItemConstructorTest() {
		// Test eigentlich javamoney.Money und ist daher unnötig
		try {
			new SportItem("Fußball", null, Money.of(10, EURO), "Ball");
			fail("Should have Failed");
		} catch (AssertionFailedError | IllegalArgumentException ignored) {

		}
		try {
			new SportItem(null, Money.of(10, EURO), Money.of(10, EURO), "lol");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored) {

		}

		try {
			new SportItem("", Money.of(10, EURO), Money.of(10, EURO), "lol");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored) {

		}

		try {
			new SportItem("Ball", Money.of(10, EURO), Money.of(10, EURO), null);
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored) {

		}

		try {
			new SportItem("Ball", Money.of(10, EURO), Money.of(10, EURO), "");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored) {

		}

		try {
			new SportItem();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		new SportItem();
	}

	@Test
	@DisplayName("Test getName()")
	public void SportItemGetNameTest() {
		// testet salespoint interne function was unnötig ist
		String name = "schlumi";

		SportItem sportItem = new SportItem(name, Money.of(10, EURO), Money.of(10, EURO), "lol");

		assertEquals(name, sportItem.getName());
	}

	@Test
	@DisplayName("Test getDeposit()")
	public void SportItemGetDepositTest() {
		SportItem sportItem = new SportItem("s", Money.of(10, EURO), Money.of(10, EURO), "lol");

		assertEquals(Money.of(10, EURO), sportItem.getDeposit());
	}

	@Test
	@DisplayName("Test setDeposit()")
	public void SportItemSetDepositTest() {
		Money depo = Money.of(10, EURO);

		SportItem sportItem = new SportItem("S", Money.of(10, EURO), depo, "lol");
		sportItem.setDeposit(depo);

		assertEquals(depo, sportItem.getDeposit());
	}

	@Test
	@DisplayName("Test getAmount()")
	public void SportItemGetAmountTest() {
		int amount = 15;
		SportItem sportItem = new SportItem("S", Money.of(10, EURO), Money.of(10, EURO), "lol", amount);

		assertEquals(amount, sportItem.getAmount());
	}

	@Test
	@DisplayName("Test setAmount()")
	public void SportItemSetAmountTest() {
		int amount = 13;
		SportItem sportItem = new SportItem("S", Money.of(10, EURO), Money.of(10, EURO), "lol");
		sportItem.setAmount(amount);
		assertEquals(amount, sportItem.getAmount());

	}

}
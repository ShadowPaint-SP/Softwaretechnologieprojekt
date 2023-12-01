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
	public void SportItemConstructorTest(){
		// Test eigentlich javamoney.Money und ist daher unnötig
		try {
			new SportItem("Fußball", null,"Ball");
			fail("Should have Failed");
		} catch (AssertionFailedError | IllegalArgumentException ignored){

		}
		try {
			new SportItem(null, Money.of(10,EURO), "lol");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored){

		}

		try {
			new SportItem("", Money.of(10,EURO), "lol");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored){

		}

		try {
			new SportItem("Ball", Money.of(10,EURO), null);
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored){

		}


		try {
			new SportItem("Ball", Money.of(10,EURO), "");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored){

		}
		try {
			new SportItem("Ball", Money.of(10,EURO), "");
			fail("Should have Failed");
		} catch (IllegalArgumentException ignored){

		}
		try {
			new SportItem();
		} catch (Exception e){
			fail(e.getMessage());
		}
		new SportItem();
	}

	@Test
	@DisplayName("Test getName()")
	public void SportItemGetNameTest(){
		// testet salespoint interne function was unnötig ist
		String name = "schlumi";

		SportItem sportItem = new SportItem(name, Money.of(10, EURO), "lol");

		assertEquals(name, sportItem.getName());
	}

	@Test
	@DisplayName("Test Equals und hashCode Methods")
	public void SportItemEqualsAndHashCodeTest(){
		SportItem s1 = new SportItem("Ball1", Money.of(10,EURO), "Bälle");

		SportItem s2 = new SportItem("Ball1", Money.of(10,EURO), "Bälle");

		assertEquals(s1,s2);

		assertEquals(s1.hashCode(),s2.hashCode());
	}
}
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
		try {
			new SportItem("Fußball", null,"Ball");
			fail("Should have Failed");
		} catch (AssertionFailedError | IllegalArgumentException ignored){

		}

	}
}
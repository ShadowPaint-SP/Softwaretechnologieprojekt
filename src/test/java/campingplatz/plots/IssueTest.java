package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

class IssueTest {

	private Issue issue;

	@Test
	public void testDefaultConstructor() {
		Issue issue = new Issue();
		assertNotNull(issue.getId(), "ID should not be null");
	}

	@BeforeEach
	void setUp() {
		this.issue = new Issue(Money.of(10, EURO), "something is broken");
	}

	@Test
	void getPrice() {
		assertEquals(Money.of(10, EURO), this.issue.getPrice());
	}

	/*
	 * @Test
	 * void getId() {
	 * //lol
	 * assertTrue(true);
	 * }
	 */

	@Test
	void getCost() {
		assertEquals(Money.of(10, EURO), this.issue.getPrice());
	}

	@Test
	void getDescription() {
		assertEquals("something is broken", this.issue.getDescription());
	}

	@Test
	void setCost() {
		this.issue.setCost(Money.of(20, EURO));
		assertEquals(Money.of(20, EURO), this.issue.getCost());
	}

	@Test
	void setDescription() {
		this.issue.setDescription("is ok");
		assertEquals("is ok", this.issue.getDescription());
	}

	@Test
	public void testGetPreDiscountPrice() {
		assertEquals(Money.of(10, "EUR"), issue.getPreDiscountPrice());
	}
}
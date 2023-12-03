package campingplatz.reservation;

import java.util.List;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;
import static org.junit.jupiter.api.Assertions.*;

import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import campingplatz.plots.Plot;

public class ReservationCartTests {
	private ReservationCart cart;
	private Reservation reservation1;
	private Reservation reservation2;

	@Mock
	private UserAccount user;

	@Mock
	private Plot plot1;

	@Mock
	private Plot plot2;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		when(plot1.getPrice()).thenReturn(Money.of(20, EURO));
		when(plot2.getPrice()).thenReturn(Money.of(30, EURO));
		when(plot1.getId()).thenReturn(Product.ProductIdentifier.of("1"));
		when(plot2.getId()).thenReturn(Product.ProductIdentifier.of("2"));

		cart = new ReservationCart();
		reservation1 = new Reservation(user, plot1, LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 10));
		cart.add(reservation1);
		reservation2 = new Reservation(user, plot2, LocalDate.of(2023, 11, 11), LocalDate.of(2023, 11, 20));
		cart.add(reservation2);
	}

	@Test
	void testAdd() {
		Reservation reservation = new Reservation(user, plot2, LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 10));
		cart.add(reservation);
		assertTrue(cart.contains(reservation));
	}

	@Test
	void testRemove() {
		cart.remove(reservation1);
		assertFalse(cart.contains(reservation1));
	}

	@Test
	void testNeighboring() {
		Reservation reservation = new Reservation(user, plot1, LocalDate.of(2023, 11, 11), LocalDate.of(2023, 11, 20));
		cart.add(reservation);
		List<Reservation> neighboring = cart.neighboring(reservation1);
		assertTrue(neighboring.contains(reservation));
	}

	@Test
	void testIntersecting() {
		Reservation reservation = new Reservation(user, plot2, LocalDate.of(2023, 11, 11), LocalDate.of(2023, 11, 15));
		List<Reservation> intersecting = cart.intersecting(reservation);
		assertFalse(intersecting.contains(reservation1));
		assertTrue(intersecting.contains(reservation2));
	}

	@Test
	void testGetPrice() {
		MonetaryAmount expectedPrice = reservation1.getPrice().add(reservation2.getPrice());
		assertEquals(expectedPrice, cart.getPrice());
	}
}
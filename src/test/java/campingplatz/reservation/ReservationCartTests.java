package campingplatz.reservation;

import javax.money.MonetaryAmount;

import campingplatz.utils.Cart;
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
	private Cart<Plot> cart;
	private PlotReservation reservation1;
	private PlotReservation reservation2;

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
		when(plot1.getName()).thenReturn("a");
		when(plot2.getName()).thenReturn("b");

		cart = new Cart<Plot>(PlotReservation.class);
		reservation1 = new PlotReservation(user, plot1, LocalDate.of(2023, 11,
			1).atStartOfDay(),
			LocalDate.of(2023, 11, 10).atStartOfDay());
		cart.add(reservation1);
		reservation2 = new PlotReservation(user, plot2, LocalDate.of(2023, 11,
			11).atStartOfDay(),
			LocalDate.of(2023, 11, 20).atStartOfDay());
		cart.add(reservation2);
	}

	@Test
	void testAdd() {

		ReservationEntry<Plot> entry = new ReservationEntry<>(plot2,
			LocalDate.of(2023, 12, 1).atStartOfDay());

		cart.add(entry);
		assertTrue(cart.contains(entry));

		PlotReservation reservation = new PlotReservation(user, plot2,
			LocalDate.of(2023, 12, 1).atStartOfDay(),
			LocalDate.of(2023, 12, 10).atStartOfDay());
		cart.add(reservation);

		var reservations = cart.getReservationsOfUser(user);

		var found = false;
		for (var res : reservations){
			if (res.getBegin().equals(LocalDate.of(2023, 12, 1).atStartOfDay())
			&&  res.getEnd().equals(LocalDate.of(2023, 12, 10).atStartOfDay())
			&& res.getUser().equals(user) && res.getProduct().equals(plot2)){
				found = true;
			}
		}

		assertTrue(found);
	}

	@Test
	void testRemove() {
		cart.remove(reservation1);
		assertFalse(cart.contains(reservation1));
	}

	@Test
	void testGetPrice() {
		MonetaryAmount expectedPrice = reservation1.getPrice().add(reservation2.getPrice());
		assertEquals(expectedPrice, cart.getPrice());
	}
}
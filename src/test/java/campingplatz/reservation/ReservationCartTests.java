package campingplatz.reservation;

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
import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;

public class ReservationCartTests {
	private PlotCart cart;
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

		cart = new PlotCart();
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

		cart.addEntry(plot2, LocalDate.of(2023, 12, 1).atStartOfDay());
		assertTrue(cart.containsEntry(plot2, LocalDate.of(2023, 12, 1).atStartOfDay()));

		PlotReservation reservation = new PlotReservation(user, plot2,
				LocalDate.of(2023, 12, 1).atStartOfDay(),
				LocalDate.of(2023, 12, 10).atStartOfDay());
		cart.add(reservation);

		cart.setUser(user);
		var reservations = cart.getReservations(user);

		var found = false;
		for (var res : reservations) {
			if (res.getBegin().equals(LocalDate.of(2023, 12, 1).atStartOfDay())
					&& res.getEnd().equals(LocalDate.of(2023, 12, 10).atStartOfDay())
					&& res.getUser().equals(user) && res.getProduct().equals(plot2)) {
				found = true;
			}
		}

		assertTrue(found);
	}

	@Test
	void testRemove() {
		assertTrue(cart.contains(reservation1));
		cart.remove(reservation1);
		assertFalse(cart.contains(reservation1));
	}
}
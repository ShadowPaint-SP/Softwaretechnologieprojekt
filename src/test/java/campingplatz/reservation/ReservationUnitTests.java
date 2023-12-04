package campingplatz.reservation;

import campingplatz.plots.Plot;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

public class ReservationUnitTests {
	private Reservation reservation;
	private Reservation reservationBordering;
	private Reservation reservationOverlapping;
	private Reservation reservationOtherPlot;

	@Mock
	private UserAccount customer;
	@Mock
	private Plot plot1;
	@Mock
	private Plot plot2;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		when(plot1.getPrice()).thenReturn(Money.of(20, EURO));
		when(plot1.getId()).thenReturn(Product.ProductIdentifier.of("1"));
		when(plot1.getId()).thenReturn(Product.ProductIdentifier.of("2"));
		reservation = new Reservation(customer, plot1, LocalDate.of(2023, 11, 1).atStartOfDay(), LocalDate.of(2023, 11, 10).atStartOfDay());
		reservationBordering = new Reservation(customer, plot1, LocalDate.of(2023, 11, 11).atStartOfDay(), LocalDate.of(2023, 11, 20).atStartOfDay());
		reservationOverlapping = new Reservation(customer, plot1, LocalDate.of(2023, 11, 10).atStartOfDay(), LocalDate.of(2023, 11, 20).atStartOfDay());
		reservationOtherPlot = new Reservation(customer, plot2, LocalDate.of(2023, 11, 15).atStartOfDay(), LocalDate.of(2023, 11, 20).atStartOfDay());
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void init_Reservation() {
		new Reservation(customer, plot1, LocalDate.of(2023, 11, 1).atStartOfDay(), LocalDate.of(2023, 11, 10).atStartOfDay());
	}

	@Test
	void getDaysTest() {
		assertEquals(reservation.duration(), ChronoUnit.DAYS.between(LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 10)),
				"reservation.getDays, wrong Days between arrival, departure");
	}

	@Test
	void getPriceTest() {
		assertEquals(reservation.getPrice(),
				plot1.getPrice().multiply(ChronoUnit.DAYS.between(LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 10))),
				"reservation.getPrice rechnet den Falschen Preis aus");
	}


}

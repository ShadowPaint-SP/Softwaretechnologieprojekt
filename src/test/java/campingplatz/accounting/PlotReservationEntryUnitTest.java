package campingplatz.accounting;

import campingplatz.customer.Customer;
import campingplatz.plots.Plot;
import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.reservation.Reservation;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
class PlotReservationEntryUnitTest {

	@Autowired
	private UserAccountManagement userAccountManagement;
	private UserAccount customer;

	private Plot plot;

	private PlotReservation reservation;


	@BeforeEach
	void setup() {
		this.customer = userAccountManagement.create("customer", Password.UnencryptedPassword.of("none"),
			Customer.Roles.CUSTOMER.getValue());
		this.plot = new Plot("1. Platz", 15.0,Money.of(20, EURO), Plot.ParkingLot.NONE, "", "");

		var arrival = LocalDate.of(2023, 11, 1).atStartOfDay();
		var departure = LocalDate.of(2023, 11, 10).atStartOfDay();

		this.reservation = new PlotReservation(this.customer, this.plot, arrival, departure);

	}
	@AfterEach
	void tearDown() {
		userAccountManagement.delete(customer);
	}
	@Test
	void PlotReservationAccountancyEntryConstructorTest() {
		assertDoesNotThrow(()->{new PlotReservationAccountancyEntry();});
		assertDoesNotThrow(()->{new PlotReservationAccountancyEntry(this.reservation);});
	}

	@Test
	void PlotReservationDeductionEntryConstructorTest() {
		assertDoesNotThrow(()->{new PlotReservationDeductionEntry();});
		assertDoesNotThrow(()->{new PlotReservationDeductionEntry(this.reservation);});
	}
}
package campingplatz.accounting;

import campingplatz.customer.Customer;
import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.seasonalplots.SeasonalPlot;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
class SeasonalPlotReservationEntryUnitTest {

	@Autowired
	private UserAccountManagement userAccountManagement;
	private UserAccount customer;

	private SeasonalPlot plot;

	private SeasonalPlotReservation reservation;


	@BeforeEach
	void setup() {
		this.customer = userAccountManagement.create("customer", Password.UnencryptedPassword.of("none"),
			Customer.Roles.CUSTOMER.getValue());
		this.plot = new SeasonalPlot("Platz an der Sonne", 90.0, Money.of(110, EURO),
			Plot.ParkingLot.CAMPER_PARKING, 50.0,
			0.0, "/img/camp_1_1/scamp_6.png", "Perfekte Sonnenuntergänge");

		var arrival = LocalDate.of(2023, 11, 1).atStartOfDay();
		var departure = LocalDate.of(2023, 11, 10).atStartOfDay();

		this.reservation = new SeasonalPlotReservation(this.customer, this.plot, arrival, departure, SeasonalPlotReservation.PayMethod.YEARLY);

	}
	@AfterEach
	void tearDown() {
		userAccountManagement.delete(customer);
	}
	@Test
	void SeasonalPlotReservationAccountancyEntryConstructorTest() {
		assertDoesNotThrow(()->{new SeasonalPlotReservationAccountancyEntry();});
		assertDoesNotThrow(()->{new SeasonalPlotReservationAccountancyEntry(this.reservation);});
	}

	@Test
	void SeasonalPlotReservationDeductionEntryConstructorTest() {
		assertDoesNotThrow(()->{new SeasonalPlotReservationDeductionEntry();});
		assertDoesNotThrow(()->{new SeasonalPlotReservationDeductionEntry(this.reservation);});
	}
}
package campingplatz.reservation;

import campingplatz.customer.Customer;
import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalog;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class ReservationUnitTests {

	@Autowired
	private PlotReservationRepository reservationRepository;

	@Autowired
	private PlotCatalog plotCatalog;

	@Autowired
	private UserAccountManagement userAccountManagement;

	private PlotReservation reservation;
	private PlotReservation takenReservation;

	private UserAccount customer;
	private Plot plot;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		reservationRepository.deleteAll();

		customer = userAccountManagement.create("customer", Password.UnencryptedPassword.of("none"),
				Customer.Roles.CUSTOMER.getValue());
		plot = new Plot("1. Platz", 15.0, Money.of(20, EURO), Plot.ParkingLot.NONE, "", "");
		plotCatalog.save(plot);

		var arrival = LocalDate.of(2023, 11, 1).atStartOfDay();
		var departure = LocalDate.of(2023, 11, 10).atStartOfDay();

		reservation = new PlotReservation(customer, plot, arrival, departure);
		reservationRepository.save(reservation);

		takenReservation = new PlotReservation(customer, plot, arrival, departure);
		takenReservation.setState(Reservation.State.TAKEN);
		reservationRepository.save(takenReservation);
	}

	@AfterEach
	void tearDown() {
		reservationRepository.deleteAll();
		plotCatalog.delete(plot);
		userAccountManagement.delete(customer);
	}

	@Test
	void init_Reservation() {
		reservation = new PlotReservation(customer, plot,
				LocalDate.of(2023, 11, 1).atStartOfDay(),
				LocalDate.of(2023, 11, 10).atStartOfDay());
	}

	@Test
	void getDaysTest() {
		assertEquals(reservation.duration(),
				ChronoUnit.DAYS.between(LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 10).plusDays(1)),
				"reservation.getDays, wrong Days between arrival, departure");
	}

	@Test
	void getPriceTest() {
		assertEquals(reservation.getPrice(),
				plot.getPrice()
						.multiply(ChronoUnit.DAYS.between(LocalDate.of(2023, 11, 1), LocalDate.of(2023, 11, 10).plusDays(1))),
				"reservation.getPrice rechnet den Falschen Preis aus");
	}

	@Test
	void FirstDeletionTest() {
		// nothing should get deleted
		reservationRepository.deleteBeforeThan(LocalDate.of(2023, 10, 30).atStartOfDay());
		assertEquals(2, reservationRepository.findAll().size());
	}

	@Test
	void SecondDeletionTest() {
		// one should get deleted
		reservationRepository.deleteBeforeThan(LocalDate.of(2023, 11, 2).atStartOfDay());
		assertEquals(1, reservationRepository.findAll().size());
		assertEquals(Optional.empty(), reservationRepository.findById(reservation.id));
		assertTrue(reservationRepository.findById(takenReservation.id).isPresent());
	}

	@Disabled
	@Test
	void ThirdDeletionTest() {
		// all should get deleted
		reservationRepository.deleteBeforeThan(LocalDate.of(2023, 11, 20).atStartOfDay());
		assertEquals(0, reservationRepository.findAll().size());
	}

}

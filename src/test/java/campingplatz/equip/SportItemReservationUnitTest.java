/*
package campingplatz.equip;

import campingplatz.customer.Customer;
import campingplatz.reservation.ReservationRepository;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
class SportItemReservationUnitTest {
	@Autowired
	private ReservationRepository<SportItem> reservationRepository;

	@Autowired
	private SportItemCatalog sportItemCatalog;

	@Autowired
	private UserAccountManagement userAccountManagement;

	private UserAccount customer;
	private SportItem sportItem;
	private SportItemReservation defaultReservation;

	private LocalDateTime defaultBegin;

	private LocalDateTime defaultEnd;

	@BeforeEach
	void before_each(){
		MockitoAnnotations.openMocks(this);

		customer = userAccountManagement.create("customer",
			Password.UnencryptedPassword.of("none"),
			Customer.Roles.CUSTOMER.getValue());
		sportItem = new SportItem("Ballo",
			Money.of(10, EURO),
			Money.of(100, EURO),
			"Ballero",
			1000,
			"empty",
			"empty");
		sportItemCatalog.save(sportItem);

		defaultBegin = LocalDateTime.of(2023, 11, 1, 14,30, 1);
		defaultEnd = LocalDateTime.of(2023, 11, 10, 15, 40, 20);

		defaultReservation = new SportItemReservation(customer, sportItem, defaultBegin, defaultEnd);
		reservationRepository.save(defaultReservation);

		this.defaultReservation = new SportItemReservation();
	}

	@AfterEach
	void tearDown() {
		reservationRepository.deleteAll();
		sportItemCatalog.delete(sportItem);
		userAccountManagement.delete(customer);
	}
*/
/*	@Test
	void init_Reservation() {
		assertDoesNotThrow(()->{new SportItemReservation(customer, sportItem, defaultBegin, defaultEnd);},
			"Constructor failed");

	}

	@Test
	void getHoursTest() {
		assertEquals(defaultReservation.duration(),
			ChronoUnit.HOURS.between(defaultBegin, defaultEnd),
			"reservation.duration(), wrong Hours between begin, end");
	}

	@Test
	void getPriceTest() {
		assertEquals(defaultReservation.getPrice(),
			sportItem.getPrice().multiply(ChronoUnit.HOURS.between(defaultBegin, defaultEnd)),
			"reservation.getPrice rechnet den Falschen Preis aus");
	}*//*




}*/

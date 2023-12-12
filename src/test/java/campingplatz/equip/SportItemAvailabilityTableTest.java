package campingplatz.equip;

import campingplatz.customer.Customer;
import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class SportItemAvailabilityTableTest {

	@Autowired
	private UserAccountManagement userAccountManagement;

	public SportItemAvailabilityTable table;
	public static final Integer MAX_AMOUNT = 23;

	@Test
	public void runThrough(){
		LocalDateTime firstTime = LocalDateTime.of(2023, 11, 1, 8, 0);
		LocalDateTime lastTime = LocalDateTime.of(2023, 11, 1, 17, 0);
		SportItem sportItem = new SportItem("Ballo",
			Money.of(10, EURO),
			Money.of(100, EURO),
			"Ballero",
			1000,
			"empty",
			"empty");


		this.table = new SportItemAvailabilityTable(firstTime, lastTime, sportItem);
		testConstructorAndAddMaxAmount();
		testAddReservation(sportItem);
		testAddSelections(sportItem);
	}


	public void testConstructorAndAddMaxAmount(){
		this.table.addMaxAmount(MAX_AMOUNT);

		for (int i = 0; i < table.length; i++){
			var field = table.get(i);
			assertEquals(MAX_AMOUNT, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.FREE_COMPLETELY, field.type);
			assertEquals(i, field.index);
		}
	}

	public void testAddReservation(SportItem sportItem){
		UserAccount customer = userAccountManagement.create("customer",
			Password.UnencryptedPassword.of("none"),
			Customer.Roles.CUSTOMER.getValue()
		);
		UserAccount other = userAccountManagement.create("other customer",
			Password.UnencryptedPassword.of("none"),
			Customer.Roles.CUSTOMER.getValue()
		);
		List<SportItemReservation> reservations = new ArrayList<>();
		reservations.add(new SportItemReservation(customer, sportItem,
			LocalDateTime.of(2023, 11, 1, 8, 0),
			LocalDateTime.of(2023, 11, 1, 13, 0)

		));
		reservations.add(new SportItemReservation(other, sportItem,
			LocalDateTime.of(2023, 11, 1, 11, 0),
			LocalDateTime.of(2023, 11, 1, 15, 0)
		));
		for (var i = 0; i < MAX_AMOUNT; i++){
			reservations.add(new SportItemReservation(other, sportItem,
				LocalDateTime.of(2023, 11, 1, 17, 0),
				LocalDateTime.of(2023, 11, 1, 17, 0)
			));
		}

		this.table.addReservations(Optional.of(customer), reservations);

		for (int i = 0; i < 3; i++){
			var field = table.get(i);

			assertEquals(MAX_AMOUNT - 1, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.RESERVED_SELF, field.type);
		}
		for (int i = 3; i < 6; i++){
			var field = table.get(i);

			assertEquals(MAX_AMOUNT - 2, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.RESERVED_SELF, field.type);
		}
		for (int i = 6; i < 8; i++){
			var field = table.get(i);

			assertEquals(MAX_AMOUNT - 1, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.FREE_COMPLETELY, field.type);
		}
		for (int i = 8; i < 9; i++){
			var field = table.get(i);

			assertEquals(MAX_AMOUNT, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.FREE_COMPLETELY, field.type);
		}
		for (int i = 9; i < 10; i++){
			var field = table.get(i);

			assertEquals(0, field.amount);
			assertEquals(SportItemAvailabilityTable.FieldType.RESERVED_OTHER, field.type);
		}
	}

	public void testAddSelections(SportItem sportItem){

		SportItemCart cart = new SportItemCart();

		cart.add(new SportItemReservation(null, sportItem,
			LocalDateTime.of(2023, 11, 1, 14, 0),
			LocalDateTime.of(2023, 11, 1, 16, 0)
		));

		this.table.addSelections(cart);

		for (int i = 0; i < 6; i++){
			var field = table.get(i);
			assertNotEquals(SportItemAvailabilityTable.FieldType.FREE_SELECTED, field.type);
		}
		for (int i = 6; i < 9; i++){
			var field = table.get(i);
			assertEquals(SportItemAvailabilityTable.FieldType.FREE_SELECTED, field.type);
		}

		for (int i = 9; i < 10; i++){
			var field = table.get(i);
			assertNotEquals(SportItemAvailabilityTable.FieldType.FREE_SELECTED, field.type);
		}


	}




}

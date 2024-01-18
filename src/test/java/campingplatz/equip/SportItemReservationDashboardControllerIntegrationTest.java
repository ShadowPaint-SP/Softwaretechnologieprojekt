package campingplatz.equip;


import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.equip.sportsitemreservations.SportItemReservationDashboardController;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SportItemReservationDashboardControllerIntegrationTest {


	@Mock
	private UserAccount user;


	private SportItemCart sportItemCart;


	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	public void testGetReservationDashboard() throws Exception {
		// Arrange
		SportItem sportItem = new SportItem("Fußball", Money.of(0.5, EURO),
			Money.of(16, EURO), "Ball", 10,
			"/img/equip/football.png",
			"Fußball macht Spaß");

		SportItem sportItem2 = new SportItem("Volleyball", Money.of(0.5, EURO),
			Money.of(14, EURO), "Volleyball", 6,
			"/img/equip/volleyball.png",
			"Volleyball macht Spaß");

		SportItemReservation reservation = new SportItemReservation(user,
			sportItem,
			LocalDateTime.of(2023, 12, 12, 11, 00),
			LocalDateTime.of(2023, 12, 12, 13, 00));

		SportItemReservation reservation2 = new SportItemReservation(user,
			sportItem2,
			LocalDateTime.of(2023, 12, 12, 16, 00),
			LocalDateTime.of(2023, 12, 12, 17, 00));

		sportItemCart = new SportItemCart();
		sportItemCart.add(reservation);
		sportItemCart.add(reservation2);


		List<SportItemReservation> reservations = new ArrayList<>();
		reservations.add(reservation);
		reservations.add(reservation2);

		assertTrue(reservations.contains(reservation));


		SportItemReservationRepository mockRepository = mock(SportItemReservationRepository.class);
		doReturn(reservations).when(mockRepository).findAll();

		SportItemReservationDashboardController controller = new SportItemReservationDashboardController(mockRepository, null);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();


		mockMvc.perform(get("/management/sportitems/reservation"))
			.andExpect(status().isOk())
			.andExpect(view().name("dashboards/reservation_sportitems_management"))
			.andExpect(model().attribute("reservationsSportItem", equalTo(reservations)));

		verify(mockRepository, times(1)).findAll();
	}

}
package campingplatz.equip;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SportItemCatalogControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SportItemCatalog sportItemCatalog;

	@MockBean
	private SportItemReservationRepository reservationRepository;

	@MockBean
	private SportItemCart cart;

	@Test
	void testSetupCatalog() throws Exception {
		SportItem sportItem = new SportItem("Fußball", Money.of(0.5, EURO),
				Money.of(16, EURO), "Ball", 10,
				"/img/equip/football.png",
				"Fußball macht Spaß");

		when(sportItemCatalog.findAll()).thenReturn(Streamable.of(sportItem));

		mockMvc.perform(get("/sportitemcatalog"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("items"))
				.andExpect(model().attribute("items", Streamable.of(sportItem).toList()));
	}
}

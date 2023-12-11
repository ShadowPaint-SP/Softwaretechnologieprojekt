package campingplatz.reservation;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTests {

	@Autowired
	MockMvc mvc;
	//generell hat hier nichts mit der Reservierung zu tun.
	@Test
	void preventsPublicAccessForCart() throws Exception {

		mvc.perform(get("/cart"))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));
	}

	@Test
	@WithMockUser(username = "boss@mail.de", roles = "BOSS")
	void cartIsAccessibleForAdmin() throws Exception {

		mvc.perform(get("/cart"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotCart"));
	}

	@Test
	@WithMockUser(username = "hans@mail.de", roles = "CUSTOMER")
	void cartIsAccessibleForCustomer() throws Exception {

		mvc.perform(get("/cart"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("plotCart"));
	}

	@Test
	@WithMockUser(username = "hans@mail.de", roles = "CUSTOMER")
	void executeReservation() throws Exception {
		// hää wo wird hier executed?
		mvc.perform(get("/plotcatalog"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("allPlots"));

	}

	@Test
	@WithMockUser(username = "hans@mail.de", roles = "CUSTOMER")
	void TPR01() throws Exception {

	}

}

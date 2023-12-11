package campingplatz.accounting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountancyDashboardControllerIntegrationTest {
	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(username = "boss@mail.de", roles = "BOSS")
	void accountyncyTest() throws Exception {
		mvc.perform(get("/management/accountancy"))
			.andExpectAll(status().isOk(),
				model().attributeExists("accountancy"));

	}
}
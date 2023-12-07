package campingplatz.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * DashboardTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "boss@mail.de", roles = "BOSS")
	public void DashboardReturnSportequipmentmanagement() throws Exception {
		mockMvc.perform(get("/management/sportsequipment")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "hans@mail.de", roles = "CUSTOMER")
	public void DashboardDoesntReturnSportequipmentmanagement() throws Exception {
		mockMvc.perform(get("/management/sportsequipment")).andExpect(status().is(403));
	}

	@Test
	void DashboardDoesntReturnSportequipmentmanagementToRandos() throws Exception {
		mockMvc.perform(get("/management/sportsequipment")).andExpect(status().isFound());
	}
}
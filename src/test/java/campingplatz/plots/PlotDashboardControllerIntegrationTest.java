package campingplatz.plots;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlotDashboardControllerIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void plotsTest() throws Exception{
		mockMvc.perform(get("/management/plots"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("plots"));
	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void changePlotDetailsTestInvalidPlotID() throws Exception{
		mockMvc.perform(post("/management/plots/updatePlot")
				.param("plotID", "lolo")
				.param("name", "Ein Plot")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("price", "120")
				.param("picture", "picture")
				.param("description", "description"))
			.andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void changePlotDetailsTestValidPlotID() throws Exception{


		mockMvc.perform(post("/management/plots/updatePlot")
				.param("plotID", "lolo")
				.param("name", "Ein Plot")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("price", "120")
				.param("picture", "picture")
				.param("description", "description"))
			.andExpect(status().isOk());

	}

	@Test
	void createPlotTest() {
	}

	@Test
	void deletePlotTest() {
	}
}
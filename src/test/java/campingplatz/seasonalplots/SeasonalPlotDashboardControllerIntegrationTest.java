package campingplatz.seasonalplots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SeasonalPlotDashboardControllerIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void seasonalPlots() throws Exception {
		mockMvc.perform(get("/management/seasonalplot"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("seasonalPlots"));
	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void changeSeasonalPlotDetails() throws Exception {
		// extract real plot
		MvcResult result = mockMvc.perform(get("/management/seasonalplot")).andReturn();

		@SuppressWarnings("unchecked")
		Streamable<SeasonalPlot> testList = (Streamable<SeasonalPlot>) Objects
				.requireNonNull(result.getModelAndView())
				.getModelMap()
				.getAttribute("seasonalPlots");
		assertNotNull(testList, "testList is null");
		SeasonalPlot testItem = testList.stream().toList().get(0);

		mockMvc.perform(post("/management/seasonalplot/updateSeasonalPlot")
				.param("plotID", String.valueOf(testItem.getId()))
				.param("name", "lolol")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("electricityMeter", "1")
				.param("waterMeter", "1")
				.param("price", "1")
				.param("description", "so lange Beschreibung")
				.param("picture", "picturePath")
				.param("state", "1"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void createSeasonalPlot() throws Exception {
		mockMvc.perform(post("/management/seasonalplot/createSeasonalPlot")
				.param("name", "lolol")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("electricityMeter", "1")
				.param("waterMeter", "1")
				.param("price", "1")
				.param("description", "so lange Beschreibung")
				.param("picture", "picturePath")
				.param("state", "1"))
				.andExpect(status().isOk());
	}
}
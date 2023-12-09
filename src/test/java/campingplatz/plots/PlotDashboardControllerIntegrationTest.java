package campingplatz.plots;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
		MvcResult result =mockMvc.perform(get("/management/plots")).andReturn();
		@SuppressWarnings("unchecked")
		Streamable<Plot> testList = (Streamable<Plot>) result.getModelAndView().
			getModelMap().
			getAttribute("plots");
		Plot testItem = testList.stream().toList().get(0);

		mockMvc.perform(post("/management/plots/updatePlot")
				.param("plotID", String.valueOf(testItem.getId()))
				.param("name", "Ein Plot")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("price", "35")
				.param("picture", "picture")
				.param("description", "description"))
			.andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void createPlotTest() throws Exception {
		mockMvc.perform(post("/management/plots/createPlot")
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
	void deletePlotTest() throws Exception {
		MvcResult result =mockMvc.perform(get("/management/plots")).andReturn();
		@SuppressWarnings("unchecked")
		Streamable<Plot> testList = (Streamable<Plot>) result.getModelAndView().
			getModelMap().
			getAttribute("plots");
		Plot testItem = testList.stream().toList().get(0);

		mockMvc.perform(post("/management/plots/deletePlot")
				.param("plotID", String.valueOf(testItem.getId()))
				.param("name", "Ein Plot")
				.param("size", "30")
				.param("parkingValue", "1")
				.param("price", "120")
				.param("picture", "picture")
				.param("description", "description"))
			.andExpect(status().isOk());
	}
}
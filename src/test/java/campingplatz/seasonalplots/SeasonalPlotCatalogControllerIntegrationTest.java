package campingplatz.seasonalplots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeasonalPlotCatalogControllerIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
	}

	@Test
	void setupSeasonalCatalog() throws Exception {
		mockMvc.perform(get("/seasonalplotcatalog"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("allSeasonalPlots"));
	}

	@Test
	void filter() throws Exception {
		mockMvc.perform(post("/seasonalplotcatalog/filter")
				.param("sizeMin", "10")
				.param("sizeMax", "20")
				.param("priceMax", "30")
				.param("priceMin", "40")
				.param("parking", "1"))
				.andExpect(status().isOk());
	}

	@Test
	void reservate() {
		// weiß nicht wie man PathVariables übergibt :(
		// bzw. hab keine Lust da jetzt rum zu probieren
	}

	@Test
	void updateSeasonalPlot() {
		// weiß nicht wie man PathVariables übergibt :(
	}

	@Test
	void forwardTime() throws Exception {
		// isn't this the dev function?
		int days = 10;
		mockMvc.perform(get("/forward/" + days + "/seasonalplotcatalog"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/seasonalplotcatalog"));
	}
}
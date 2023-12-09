package campingplatz.plots;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlotCatalogControllerIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@Test
	void TPK01() throws Exception{
		//entspricht TPK01 aus dem Pflichtenheft
		mockMvc.perform(get("/plotcatalog"))
			.andExpect(model().attributeExists("allPlots"));

	}

}
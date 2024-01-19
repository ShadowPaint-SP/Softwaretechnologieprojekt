package campingplatz.plots;

import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


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
			.andExpect(model().attributeExists("filteredPlots"));

	}


	@Autowired 
	PlotCatalog plotCatalog;
	
	
	@Test
    @WithMockUser 
    void TPK02() throws Exception {
		ProductIdentifier testId =plotCatalog.findAll().iterator().next().getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/plotcatalog/details/{plot}", testId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("servings/plotdetails"));
    }

    @Test
    @WithMockUser (username = "meister@mail.de", roles = "EMPLOYEE")
    void TPK03() throws Exception {
		//Comment from valid User
		ProductIdentifier testId = plotCatalog.findAll().iterator().next().getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plotcatalog/details/{plot}/comments", testId)
                .param("comment", "Test comment")
                .param("rating", "5"))
                .andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/plotcatalog/details/"+testId));
    }


	@Test
    @WithMockUser (username = "hans@mail.de", roles = "CUSTOMER")
    void TPK04() throws Exception { 
		//Comment from invalid User
		ProductIdentifier testId =plotCatalog.findByName("Platz am See I").iterator().next().getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/plotcatalog/details/{plot}/comments", testId)
                .param("comment", "Test comment")
                .param("rating", "5"))
                .andExpect(status().is2xxSuccessful())
				.andExpect(redirectedUrl(null));
    }


}
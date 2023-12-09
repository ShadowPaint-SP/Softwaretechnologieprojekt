package campingplatz.equip;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SportItemCatalogControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void TDA11() throws Exception {
		//entspricht TDA11 aus dem Pflichtenheft
		mvc.perform(get("/management/sportsequipment"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("items"));
		// here one item should be extracted, which can be passed down

		mvc.perform(post("/addSportItem")
			.param("name", "Fußball")
			.param("price", "12")
			.param("deposit", "30")
			.param("amount","15")
			.param("category", "Ball")
			.param("imagePath", "path")
			.param("desc", "description"))
			.andExpect(status().is3xxRedirection());

		//check change here
		mvc.perform(get("/management/sportsequipment"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("items"));



	}
}
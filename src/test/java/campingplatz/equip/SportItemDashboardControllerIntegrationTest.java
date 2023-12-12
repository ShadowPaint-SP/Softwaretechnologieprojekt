package campingplatz.equip;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SportItemDashboardControllerIntegrationTest{
	@Autowired
	MockMvc mockMvc;

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void setUpTest()throws Exception {
		mockMvc.perform(get("/sportitemcatalog"))
			.andExpectAll(status().isOk(),
				model().attributeExists("items"));
	}



	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void TDA12() throws Exception {

		MvcResult result = mockMvc.perform(get("/management/sportsequipment"))
			.andExpect(status().isOk())
			.andExpect(view().name("dashboards/sportsequipment_management"))
			.andReturn();


		@SuppressWarnings("unchecked")
		List<SportItem> testList = (List<SportItem>) result.getModelAndView()
			.getModelMap()
			.getAttribute("items");


		mockMvc.perform(post("/addSportItem")
				.param("name", "Flummi")
				.param("price", "100")
				.param("deposit", "50")
				.param("amount", "1")
				.param("category", "Spaß")
				.param("imagePath", "Bild")
				.param("desc", "Beschreibung"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/management/sportsequipment"));


		MvcResult result1 = mockMvc.perform(get("/management/sportsequipment"))
			.andReturn();

		@SuppressWarnings("unchecked")
		List<SportItem> testList1 = (List<SportItem>) result1.getModelAndView()
			.getModelMap()
			.getAttribute("items");

		assertEquals(testList.size() + 1, testList1.size());
	}


	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void TDA14()throws Exception {
		//entspricht TDA14 aus dem Pflichtenheft
		MvcResult result =mockMvc.perform(get("/management/sportsequipment")).andReturn();
		@SuppressWarnings("unchecked")
		List<SportItem> testList = (List<SportItem>) result.getModelAndView().
			getModelMap().
			getAttribute("items");
		SportItem testItem = testList.get(0);
		// here one item should be extracted, which can be passed down

		mockMvc.perform(post("/deleteSportItem")
				.param("itemName", testItem.getName()))
			.andExpect(status().is3xxRedirection());


		//check change here
		MvcResult result1 =mockMvc.perform(get("/management/sportsequipment")).andReturn();
		@SuppressWarnings("unchecked")
		List<SportItem> testList1 = (List<SportItem>) result1.getModelAndView().
			getModelMap().
			getAttribute("items");
		assertEquals(testList.size() - 1 , testList1.size());
		//eigentlich nicht richtig, es könnte ja auch ein anderes Item entfernt worden sein
		//schlägt auch fehl wenn es keine SportItems gibt.


	}


	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void changeSportItemAmount() throws Exception {
		int testAmount = 10000;

		MvcResult result =mockMvc.perform(get("/management/sportsequipment")).andReturn();
		@SuppressWarnings("unchecked")
		List<SportItem> testList = (List<SportItem>) result.getModelAndView().
			getModelMap().
			getAttribute("items");
		SportItem testItem = testList.get(0);
		// extract one item to change amount

		mockMvc.perform(post("/changeSportItemAmount")
				.param("equip_id", String.valueOf(testItem.getId()))
				.param("amountItem", String.valueOf(testAmount)))
			.andExpect(status().is3xxRedirection());

		MvcResult result1 =mockMvc.perform(get("/management/sportsequipment")).andReturn();
		@SuppressWarnings("unchecked")
		List<SportItem> testList1 = (List<SportItem>) result1.getModelAndView().
			getModelMap().
			getAttribute("items");
		SportItem testItem1 = testList1.get(0);

		assertEquals(testAmount, testItem1.getAmount());
	}





}


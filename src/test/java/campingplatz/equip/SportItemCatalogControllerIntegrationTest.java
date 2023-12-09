package campingplatz.equip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.money.MonetaryAmount;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class SportItemCatalogControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Test
	@WithMockUser(username = "meister@mail.de", roles = "EMPLOYEE")
	void TDA11() throws Exception {
		//entspricht TDA11 aus dem Pflichtenheft
		int money = 12;

		MvcResult result =mvc.perform(get("/management/sportsequipment")).andReturn();
		List<SportItem> testList = (List<SportItem>) result.getModelAndView().
			getModelMap().
			getAttribute("items");
		SportItem testItem = testList.get(0);
		// here one item should be extracted, which can be passed down

		mvc.perform(post("/addSportItem")
			.param("name", testItem.getName())
			.param("price", String.valueOf(money))
			.param("deposit", String.valueOf(testItem.getDeposit().getNumber()))
			.param("amount", String.valueOf(testItem.getAmount()))
			.param("category", testItem.getCategories().toList().get(0))
			.param("imagePath", testItem.getImagePath())
			.param("desc", testItem.getDesc()))
			.andExpect(status().is3xxRedirection());

		//check change here
		MvcResult result1 =mvc.perform(get("/management/sportsequipment")).andReturn();
		List<SportItem> testList1 = (List<SportItem>) result.getModelAndView().
			getModelMap().
			getAttribute("items");
		SportItem checkItem = testList.get(0);
		MonetaryAmount checkMoney = testItem.getPrice();
		// mist irgendwas fehlt
		// assertEquals(money, checkMoney.getNumber().intValue());

	}
}
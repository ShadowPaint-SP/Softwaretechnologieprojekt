package campingplatz.customer;

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
class CustomerManagementIntegrationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void TAM01RegisterAndLogin() throws Exception {
	//entspricht TAM01 aus dem Pflichtenheft
		mvc.perform(post("/register")
			.param("name", "jannes")
			.param("last", "unwichtig")
			.param("password", "12HG875tG")
			.param("email", "jannes@mail.de"))
			.andExpectAll(
			status().is3xxRedirection(),
			redirectedUrl("/default/login"));

		mvc.perform(post("/login")
				.param("password", "12HG875tG")
				.param("username", "jannes@mail.de"))
			.andExpectAll(
				status().is3xxRedirection(),
				redirectedUrl("/"));

	}
	@Test
	@WithMockUser(username = "jannes@mail.de")
	void TAM01Rights() throws Exception {
		//entspricht TAM01 aus dem Pflichtenheft
		mvc.perform(get("/cart"))
			.andExpect(status().isOk());

		mvc.perform(get("/orders"))
			.andExpect(status().isOk());

		mvc.perform(get("/management/reservation"))
			.andExpect(status().is4xxClientError());


	}
	@Test
	void TAM02Register() throws Exception {
		//entspricht TAM02 aus dem Pflichtenheft
		mvc.perform(post("/register")
				.param("name", "jörg")
				.param("last", "unwichtig")
				.param("password", "1823h7og1")
				.param("email", "joerg@mail.de"))
			.andExpectAll(
				status().is3xxRedirection(),
				redirectedUrl("/default/login"));

		mvc.perform(post("/register")
				.param("name", "jörg")
				.param("last", "unwichtig")
				.param("password", "1823h7og1")
				.param("email", "joerg@mail.de")).
			andExpect(model().attribute("errorMessage", "User with this email already exists!"));

	}

	@Test
	void TAM03Login() throws Exception {
		//entspricht TAM03 aus dem Pflichtenheft, funktioniert nur mit TAM02Register()
		mvc.perform(post("/login")
				.param("password", "1823h7og1")
				.param("username", "joerg@mail.de"))
			.andExpectAll(
				status().is3xxRedirection(),
				redirectedUrl("/"));


	}
	@Test
	@WithMockUser(username = "joerg@mail.de")
	void TAM03Rights() throws Exception {
		//entspricht TAM03 aus dem Pflichtenheft, funktioniert nur mit TAM02Register()
		mvc.perform(get("/cart"))
			.andExpect(status().isOk());

		mvc.perform(get("/orders"))
			.andExpect(status().isOk());

		mvc.perform(get("/management/reservation"))
			.andExpect(status().is4xxClientError());


	}

}
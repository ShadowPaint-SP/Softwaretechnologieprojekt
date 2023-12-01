package campingplatz.equip;

import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SportItemCatalogController {

	private SportItemCatalog itemCatalog;

	SportItemCatalogController(SportItemCatalog itemCatalog){
		this.itemCatalog = itemCatalog;
	}

	@GetMapping("/equip")
	String setupCatalog(Model model, @LoggedIn Optional<UserAccount> user){

		List<SportItem> listo = this.itemCatalog.findAll().stream().collect(Collectors.toList());

		model.addAttribute("items", listo);

		return "equip";
	}

}

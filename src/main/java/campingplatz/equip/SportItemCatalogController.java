package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class SportItemCatalogController {

	private SportItemCatalog itemCatalog;

	SportItemCatalogController(SportItemCatalog itemCatalog) {
		this.itemCatalog = itemCatalog;
	}

	@GetMapping("/sportequipmentcatalog")
	String setupCatalog(Model model, @LoggedIn Optional<UserAccount> user) {

		List<SportItem> listo = this.itemCatalog.findAll().stream().collect(Collectors.toList());

		model.addAttribute("items", listo);

		return "servings/sportequipmentcatalog";
	}

	@PostMapping("/addSportItem")
	public String addSportItem(@RequestParam String name,
			@RequestParam double price,
			@RequestParam double deposit,
			@RequestParam int amount,
			@RequestParam(required = false) Product.ProductIdentifier itemId) {

		if (itemId != null) {

			SportItem oldItem = itemCatalog.findByName(name).stream().findFirst().orElse(null);
			if (oldItem != null) {
				oldItem.setAmount(oldItem.getAmount() + amount);
				itemCatalog.save(oldItem);
			}
		} else {
			SportItem newItem = new SportItem(name, Money.of(price, EURO), Money.of(deposit, EURO), " ", amount);
			itemCatalog.save(newItem);
		}

		return "redirect:/sportequipmentcatalog";
	}

	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasRole('BOSS')")
	String sportsequipment(Model model) {
		return "dashboards/sportsequipment_mamangement";
	}
}

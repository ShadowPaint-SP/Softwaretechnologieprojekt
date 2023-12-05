package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	String setupCatalog(Model model) {
		List<SportItem> listo = this.itemCatalog.findAll().stream().collect(Collectors.toList());

		model.addAttribute("items", listo);

		return "servings/sportequipmentcatalog";
	}

	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasRole('BOSS')")
	public String setup(Model model) {

		List<SportItem> listo = this.itemCatalog.findAll().stream().collect(Collectors.toList());

		model.addAttribute("items", listo);

		return "dashboards/sportsequipment_mamangement";
	}
/*
	@GetMapping({"/sportequipmentcatalog", "/management/sportsequipment"})
	public String setupCatalogAndManagement(Model model, @LoggedIn Optional<UserAccount> user) {
		List<SportItem> sportItems = this.itemCatalog.findAll().stream().collect(Collectors.toList());
		model.addAttribute("items", sportItems);

		if (user.isPresent() && user.get().hasRole(Role.of("BOSS"))){
			return "dashboards/sportsequipment_mamangement";
		} else {
			return "servings/sportequipmentcatalog";
		}
	}*/


	@GetMapping("/item/{id}")
	public String showSportItemDetails(@PathVariable Product.ProductIdentifier id, Model model) {
		Optional<SportItem> sportItemOptional = itemCatalog.findById(id);

		if (sportItemOptional.isPresent()) {
			SportItem sportItem = sportItemOptional.get();
			model.addAttribute("item", sportItem);
			return "servings/sportitemdetails";
		} else {

			return "servings/sportequipmentcatalog";
		}
	}

/*
	@PostMapping("/addSportItem")
	public String addSportItem(@RequestParam String name,
							   @RequestParam double price,
							   @RequestParam double deposit,
							   @RequestParam int amount,
							   @RequestParam(required = false) Product.ProductIdentifier itemId,
							   @RequestParam String category) {

		if (itemId != null) {
			SportItem oldItem = itemCatalog.findByName(name).stream().findFirst().orElse(null);
			if (oldItem != null) {
				oldItem.setAmount(oldItem.getAmount() + amount);
				itemCatalog.save(oldItem);
			}
		}
			//SportItem newItem = new SportItem(name, Money.of(price, EURO), Money.of(deposit, EURO), " ", amount);
		itemCatalog.save(new SportItem(name, Money.of(price, EURO), Money.of(deposit, EURO),category, amount));
		return "redirect:/management/sportsequipment";

	}*/

	@PostMapping("/addSportItem")
	public String addSportItem(@RequestParam String name,
							   @RequestParam double price,
							   @RequestParam double deposit,
							   @RequestParam int amount,
							   @RequestParam(required = false) Product.ProductIdentifier itemId,
							   @RequestParam String category) {

		SportItem item = itemCatalog.findByName(name).stream().findFirst().orElse(null);
		if(item==null) {
			itemCatalog.save(new SportItem(name, Money.of(price, EURO), Money.of(deposit, EURO), category, amount));
		}
		return "redirect:/management/sportsequipment";
	}

	@PostMapping("/changeSportItemAmount")
	public String changeSportItemAmount(@RequestParam String nameItem,
							   @RequestParam int amountItem,
							   @RequestParam(required = false) Product.ProductIdentifier equip_id) {

		if (equip_id != null) {
			SportItem item = itemCatalog.findByName(nameItem).stream().findFirst().orElse(null);
			if (item != null) {
				item.setAmount(amountItem);
				itemCatalog.save(item);
			}
		}
		return "redirect:/management/sportsequipment";

	}

	@PostMapping("/deleteSportItem")
	public String deleteSportItem(@RequestParam String itemName, @RequestParam(required = false) Product.ProductIdentifier id) {
		SportItem item = itemCatalog.findByName(itemName).stream().findFirst().orElse(null);
		if(item!=null && item.getId() != null) {
			itemCatalog.deleteById(item.getId());
		}
		return "redirect:/management/sportsequipment";

	}

}


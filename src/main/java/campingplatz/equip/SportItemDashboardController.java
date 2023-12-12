package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class SportItemDashboardController {


	private SportItemCatalog itemCatalog;

	SportItemDashboardController(SportItemCatalog itemCatalog) {
		this.itemCatalog = itemCatalog;
	}

	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String setup(Model model) {

		List<SportItem> listo = this.itemCatalog.findAll().stream().toList();

		model.addAttribute("items", listo);
		//model.addAttribute("first", listo.get(0));
		model.addAttribute("cate", listo.get(0).getCategories().stream().toList().get(0));

		return "dashboards/sportsequipment_management";
	}



	//TODO: improve urls to be of the same style used everywhere else
	@PostMapping("/addSportItem")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String addSportItem(@RequestParam String name,
							   @RequestParam double price,
							   @RequestParam double deposit,
							   @RequestParam int amount,
							   @RequestParam String category,
							   @RequestParam String imagePath,
							   @RequestParam String desc) {

		SportItem item = itemCatalog.findByName(name).stream().findFirst().orElse(null);
		if (item == null) {
			itemCatalog.save(new SportItem(name,
				Money.of(price, EURO),
				Money.of(deposit, EURO),
				category, amount,
				imagePath,
				desc));
		} else {
			item.setName(name);
			item.setPrice(Money.of(price, EURO));
			item.setDeposit(Money.of(deposit, EURO));
			item.addCategory(category); // das ist noch nicht so gut.
			item.setAmount(amount);
			item.setImagePath(imagePath);
			item.setDesc(desc);
			itemCatalog.save(item);
		}
		return "redirect:/management/sportsequipment";
	}

	@PostMapping("/changeSportItemAmount")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String changeSportItemAmount(@RequestParam int amountItem,
										@RequestParam(required = false) Product.ProductIdentifier equip_id) {

		if (equip_id != null) {
			SportItem item = itemCatalog.findById(equip_id).stream().findFirst().orElse(null);

			if (item != null) {
				item.setAmount(amountItem);
				itemCatalog.save(item);
			}
		}
		return "redirect:/management/sportsequipment";

	}

	@PostMapping("/deleteSportItem")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String deleteSportItem(@RequestParam String itemName,
								  @RequestParam(required = false) Product.ProductIdentifier id) {
		SportItem item = itemCatalog.findByName(itemName).stream().findFirst().orElse(null);
		if (item != null && item.getId() != null) {
			itemCatalog.deleteById(item.getId());
		}
		return "redirect:/management/sportsequipment";
	}
}
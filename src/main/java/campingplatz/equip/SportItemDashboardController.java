package campingplatz.equip;

import campingplatz.utils.Utils;
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


/**
 * Controller for managing sports equipment.
 * Handles operations such as viewing, adding, modifying, and deleting sport items in the management dashboard.
 */
@Controller
public class SportItemDashboardController {

	private SportItemCatalog itemCatalog;

	SportItemDashboardController(SportItemCatalog itemCatalog) {
		this.itemCatalog = itemCatalog;
	}


    /**
     * Sets up the sports equipment management dashboard for authorized users.
     *
     * @param model The Spring MVC model for passing data to the view.
     * @return The view name for the sports equipment management dashboard.
     */
	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	public String setup(Model model) {

		List<SportItem> listo = this.itemCatalog.findAll().stream().toList();

		model.addAttribute("items", listo);
		// model.addAttribute("first", listo.get(0));
		model.addAttribute("cate", listo.get(0).getCategories().stream().toList().get(0));

		return "dashboards/sportsequipment_management";
	}


    /**
     * Handles the addition or modification of a sport item in the catalog.
     *
     * @param name      The name of the sport item.
     * @param price     The price of the sport item.
     * @param deposit   The deposit required for the sport item.
     * @param amount    The quantity of the sport item.
     * @param category  The category of the sport item.
     * @param imagePath The image path for the sport item.
     * @param desc      The description of the sport item.
     * @return A redirect to the sports equipment management dashboard.
     */
	@PostMapping("/management/sportsequipment/additem")
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
			item.setName(Utils.clampLength(name));
			item.setPrice(Utils.clampPrice(Money.of(price, EURO)));
			item.setDeposit(Utils.clampPrice(Money.of(deposit, EURO)));
			item.addCategory(category); // das ist noch nicht so gut.
			item.setAmount(Math.max(amount, 0));
			item.setImagePath(Utils.clampLength(imagePath));
			item.setDesc(Utils.clampLength(desc));
			itemCatalog.save(item);
		}
		return "redirect:/management/sportsequipment";
	}


    /**
     * Handles the modification of the quantity of a sport item in the catalog.
     *
     * @param amountItem The new quantity of the sport item.
     * @param equip_id   The product identifier of the sport item.
     * @return A redirect to the sports equipment management dashboard.
     */
	@PostMapping("/management/sportsequipment/changeamount")
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


    /**
     * Handles the deletion of a sport item from the catalog.
     *
     * @param itemName The name of the sport item to be deleted.
     * @param id       The product identifier of the sport item.
     * @return A redirect to the sports equipment management dashboard.
     */
	@PostMapping("/management/sportsequipment/delete")
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

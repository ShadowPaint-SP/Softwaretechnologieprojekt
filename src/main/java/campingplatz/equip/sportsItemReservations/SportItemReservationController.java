package campingplatz.equip.sportsItemReservations;

import campingplatz.equip.SportItem;
import campingplatz.equip.SportItemCatalog;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SessionAttributes("cartSportItem")
@Controller
public class SportItemReservationController {

	private SportItemReservationRepository reservationRepositorySportItem;
	private final SportItemCatalog itemCatalog;

	SportItemReservationController(SportItemReservationRepository reservationRepositorySportItem, SportItemCatalog itemCatalog) {

		this.reservationRepositorySportItem = reservationRepositorySportItem;
		this.itemCatalog = itemCatalog;
	}

	@ModelAttribute("cartSportItem")
	SportItemCart initializeCart() {
		return new SportItemCart();
	}

	@GetMapping("/cartSportItem")
	String cart(Model model,
				@RequestParam("id") Product.ProductIdentifier id,
				@LoggedIn Optional<UserAccount> userAccount,
				@ModelAttribute("cartSportItem") SportItemCart reservationCartSportItem,
				@RequestParam("startTime") LocalDateTime startTime,
				@RequestParam("endTime") LocalDateTime endTime) {

		if (userAccount.isEmpty()) {
			return "static/defaultlogin";

		}

		UserAccount userAccount2 = userAccount.get();

		SportItem item = itemCatalog.findById(id).get();

		reservationCartSportItem.add(new SportItemReservation(userAccount2, item, startTime, endTime));
		var reservationsSport = reservationCartSportItem.getReservationsOfUser(userAccount2);
		MonetaryAmount totalPrice = reservationCartSportItem.getPrice();
		model.addAttribute("reservationsSport", reservationsSport);
		model.addAttribute("totalPrice", totalPrice);

		return "servings/cartSportItem";
	}

	@PostMapping("/reservate/")
	String reservate(Model model, @LoggedIn UserAccount userAccount,
					 @ModelAttribute("cartSportItem") SportItemCart reservationCartSportItem) {

		List<SportItemReservation> reservationsSport = reservationCartSportItem.getReservationsOfUser(userAccount);
		reservationRepositorySportItem.saveAll(reservationsSport);
		for(SportItemReservation reservation: reservationsSport){
			SportItem sportItem = reservation.getProduct();
			sportItem.setAmount(sportItem.getAmount()-1);
		}
		reservationCartSportItem.clear();

		return "servings/cartSportItem";
	}




}




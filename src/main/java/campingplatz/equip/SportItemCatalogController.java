package campingplatz.equip;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Controller for managing the Sport Item Catalog in the campingplatz application.
 * Provides functionality for users to view available sport items, make reservations,
 * and manage the sport item inventory.
 */
@Controller
@SessionAttributes("SportItemCart")
public class SportItemCatalogController {

	private SportItemCatalog itemCatalog;

	private SportItemReservationRepository reservationRepository;

	SportItemCatalogController(SportItemCatalog itemCatalog, SportItemReservationRepository reservationRepository) {
		this.itemCatalog = itemCatalog;
		this.reservationRepository = reservationRepository;
	}

    /**
     * Initializes the SportItemCart as a session attribute.
     *
     * @return A new instance of SportItemCart.
     */
	@ModelAttribute("SportItemCart")
	SportItemCart initializeCart() {
		return new SportItemCart();
	}


    /**
     * Prepares and displays the Sport Item Catalog for users.
     *
     * @param model The Spring MVC model to convey data to the view.
     * @return The view name corresponding to the sport equipment catalog.
     */
	@GetMapping("/sportitemcatalog")
	String setupCatalog(Model model) {
		List<SportItem> listo = this.itemCatalog.findAll().stream().toList();

		model.addAttribute("items", listo);

		return "servings/sportequipmentcatalog";
	}

	@GetMapping("/sportitem/{sportItem}")
	public String showSportItemDetails(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SiteState state, @PathVariable SportItem sportItem,
			@ModelAttribute("SportItemCart") SportItemCart reservationCart) {

		var currentDay = state.getDefaultedDay();
		var opening = currentDay.atStartOfDay().plusHours(9);
		var closing = currentDay.atStartOfDay().plusHours(17);
		var formatedTimes = new ArrayList<String>();
		for (var curr = opening; !curr.isAfter(closing); curr = curr.plusHours(1)) {
			formatedTimes.add(curr.format(DateTimeFormatter.ofPattern("H.mm")));
		}

		var reservations = reservationRepository.findReservationsBetween(opening, closing);

		var availabilityTable = new SportItemAvailabilityTable(opening, closing, sportItem)
				.addMaxAmount(sportItem.getAmount())
				.addReservations(user, reservations)
				.addSelections(reservationCart)
				.addPastMarkings(LocalDateTime.now());

		model.addAttribute("item", sportItem);
		model.addAttribute("times", formatedTimes);
		model.addAttribute("state", state);
		model.addAttribute("availabilityTable", availabilityTable);
		return "servings/sportitemdetails";

	}

	@PostMapping("/sportitem/{sportItem}")
	public String updateSportItemDetails(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SiteState state, @PathVariable SportItem sportItem,
			@ModelAttribute("SportItemCart") SportItemCart reservationCart) {

		return showSportItemDetails(model, user, state, sportItem, reservationCart);
	}

	interface SiteState {

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate getDay();

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		default LocalDate getDefaultedDay() {
			if (getDay() == null) {
				return LocalDate.now();
			}
			return getDay();
		}

	}

	@PostMapping("/sportitem/select/{sportitem}/{index}")
	@PreAuthorize("isAuthenticated()")
	String addReservationDay(Model model, @LoggedIn UserAccount user, @Valid SiteState state,
			@PathVariable("sportitem") SportItem sportItem, @PathVariable("index") Integer index,
			@ModelAttribute("SportItemCart") SportItemCart reservationCart) {

		var currentDay = state.getDefaultedDay();
		var time = currentDay.atStartOfDay().plusHours(9L + Long.valueOf(index));

		if (time.isBefore(LocalDateTime.now())) {
			model.addAttribute("error", "Selected time cannot be in the past.");
			return showSportItemDetails(model, Optional.ofNullable(user), state, sportItem, reservationCart);
		}

		if (!reservationCart.containsEntry(sportItem, time)) {
			reservationCart.addEntry(sportItem, time);
		} else {
			reservationCart.removeEntry(sportItem, time);
		}

		return showSportItemDetails(model, Optional.ofNullable(user), state, sportItem, reservationCart);
	}
}

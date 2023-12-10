package campingplatz.equip;

import campingplatz.equip.sportsItemReservations.SportItemReservation;
import campingplatz.equip.sportsItemReservations.SportItemReservationRepository;
import org.hibernate.mapping.Array;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class SportItemCatalogController {

	private SportItemCatalog itemCatalog;

	private SportItemReservationRepository reservationRepository;

	SportItemCatalogController(SportItemCatalog itemCatalog, SportItemReservationRepository reservationRepository) {
		this.itemCatalog = itemCatalog;
		this.reservationRepository = reservationRepository;
	}

	@GetMapping("/sportequipmentcatalog")
	String setupCatalog(Model model) {
		List<SportItem> listo = this.itemCatalog.findAll().stream().toList();

		model.addAttribute("items", listo);

		return "servings/sportequipmentcatalog";
	}


	@GetMapping("/item/{id}")
	public String showSportItemDetails(@LoggedIn UserAccount user , Model model, @PathVariable Product.ProductIdentifier id) {
		Optional<SportItem> sportItemOptional = itemCatalog.findById(id);

		if (sportItemOptional.isEmpty()){
			return "servings/sportequipmentcatalog";
		}

		var currentDay = LocalDate.now();
		var opening = currentDay.atStartOfDay().plusHours(9);
		var closing = currentDay.atStartOfDay().plusHours(17);
		var formatedTimes = new ArrayList<String>();
		for (var curr = opening; !curr.isAfter(closing); curr = curr.plusHours(1)){
			formatedTimes.add(curr.format(DateTimeFormatter.ofPattern("H")));
		}


		var sportItem = sportItemOptional.get();
		var reservations = reservationRepository.findReservationsBetween(opening, closing);


		var availabilityTable = new SportItemAvailabilityTable(opening, closing)
			.addMaxAmount(sportItem.getAmount())
			.addReservations(user, reservations);


		model.addAttribute("item", sportItem);
		model.addAttribute("times", formatedTimes);
		model.addAttribute("availabilityTable", availabilityTable);
		return "servings/sportitemdetails";

	}



}

package campingplatz.reservation;

import campingplatz.equip.SportItem;
import campingplatz.equip.SportItemCatalog;
import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({ "plotCart", "SportItemCart" })
@EnableScheduling
class ReservationController {

	private final PlotReservationRepository plotReservationRepository;
	private final SportItemReservationRepository sportItemReservationRepository;
	private SportItemCatalog itemCatalog;

	ReservationController(PlotReservationRepository plotReservationRepository,
			SportItemReservationRepository sportItemReservationRepository, SportItemCatalog itemCatalog) {

		this.plotReservationRepository = plotReservationRepository;
		this.sportItemReservationRepository = sportItemReservationRepository;
		this.itemCatalog = itemCatalog;
	}

	@ModelAttribute("plotCart")
	PlotCart initializeCart() {
		return new PlotCart();
	}

	@ModelAttribute("SportItemCart")
	SportItemCart initializeSportCart() {
		return new SportItemCart();
	}

	@GetMapping("/cart")
	String cart(Model model, @LoggedIn UserAccount userAccount,
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		var plotReservations = reservationCart.getReservationsOfUser(userAccount);
		model.addAttribute("plotReservations", plotReservations);

		var sportsReservations = sportItemCart.getReservationsOfUser(userAccount);
		model.addAttribute("sportsReservations", sportsReservations);

		var total = reservationCart.getPrice().add(sportItemCart.getPrice());
		model.addAttribute("total", total);

		return "servings/cart";
	}

	@PostMapping("/checkout")
	String reservate(Model model, @LoggedIn UserAccount userAccount,
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		List<PlotReservation> reservations = reservationCart.getReservationsOfUser(userAccount);

		// TODO: replace with a propper database query. this is slow an terrible
		for (var reservation : reservations) {
			if (plotReservationRepository.productIsAvailableIn(
					reservation.getProduct(),
					reservation.getBegin(),
					reservation.getEnd())) {
				plotReservationRepository.save(reservation);
			}
		}

		reservationCart.clear();

		List<SportItemReservation> sportReservations = sportItemCart.getReservationsOfUser(userAccount);
		sportItemReservationRepository.saveAll(sportReservations);

		sportItemReservationRepository.saveAll(sportReservations);
		for (SportItemReservation reservation : sportReservations) {
			SportItem sportItem = reservation.getProduct();
			sportItem.setAmount(sportItem.getAmount() - 1);
			itemCatalog.save(sportItem);

		}
		sportItemCart.clear();

		return "redirect:/";
	}

	@GetMapping("/orders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var userReservations = plotReservationRepository.findByUserId(user.getId());
		model.addAttribute("ordersCompleted", userReservations);
		return "servings/orders";
	}

	// we are scheduling a task to be executed at 10:00 AM every day of every month.
	// were we are deleting the reservations older than the current day if they were
	// not taken
	@Scheduled(cron = "0 00 10 * * ?")
	public void periodicallyDeleteReservatinos() {
		plotReservationRepository.deleteBeforeThan(LocalDateTime.now());
	}
}

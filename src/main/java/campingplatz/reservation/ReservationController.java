package campingplatz.reservation;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import campingplatz.plots.Plot;
import campingplatz.plots.plotdiscounts.PlotReservationDiscount;
import campingplatz.plots.plotdiscounts.PlotReservationDiscountRepository;
import campingplatz.plots.plotdiscounts.PlotReservationDiscounter;
import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;

import campingplatz.utils.Utils;
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
	private final SeasonalPlotReservationRepository seasonalPlotReservationRepository;

	private final PlotReservationDiscountRepository plotReservationDiscountRepository;

	ReservationController(PlotReservationRepository plotReservationRepository,
			SportItemReservationRepository sportItemReservationRepository,
			SeasonalPlotReservationRepository seasonalPlotReservationRepository,
			PlotReservationDiscountRepository plotReservationDiscountRepository) {

		this.plotReservationRepository = plotReservationRepository;
		this.sportItemReservationRepository = sportItemReservationRepository;
		this.seasonalPlotReservationRepository = seasonalPlotReservationRepository;

		this.plotReservationDiscountRepository = plotReservationDiscountRepository;
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

		// dont forget to set the user account before getting the reservations.
		// unfortunately this is the best I can come up with, as I cannot find a way
		// to initialize model attributes only for logged-in users or something similar
		reservationCart.setUser(userAccount);
		sportItemCart.setUser(userAccount);

		var plotReservations = reservationCart.getReservations();
		var plotDiscounts = plotReservationDiscountRepository.findAll();
		var plotDiscounter = new PlotReservationDiscounter(plotReservations, plotDiscounts);
		plotDiscounter.applyDiscountToAll(plotReservations);
		model.addAttribute("plotReservations", plotReservations);

		var sportsReservations = sportItemCart.getReservations();
		model.addAttribute("sportsReservations", sportsReservations);

		var plotFullPrice = Utils.getPrice(plotReservations);
		var sportsFullPrice = Utils.getPrice(sportsReservations);
		var totalFullPrice = plotFullPrice.add(sportsFullPrice);
		model.addAttribute("totalPrice", totalFullPrice);

		var plotDiscountedPrice = Utils.getDiscountedPrice(plotReservations);
		var sportsDiscountedPrice = Utils.getDiscountedPrice(sportsReservations);
		var totalDiscountedPrice = plotDiscountedPrice.add(sportsDiscountedPrice);
		model.addAttribute("totalDiscountedPrice", totalDiscountedPrice);

		return "servings/cart";
	}

	@PostMapping("/checkout")
	String reservate(Model model, @LoggedIn UserAccount userAccount,
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		List<PlotReservation> reservations = reservationCart.getReservations();
		for (var reservation : reservations) {
			if (plotReservationRepository.productIsAvailableIn(
					reservation.getProduct(),
					reservation.getBegin(),
					reservation.getEnd())) {
				plotReservationRepository.save(reservation);
			}
		}
		reservationCart.clear();

		List<SportItemReservation> sportReservations = sportItemCart.getReservations();
		sportItemReservationRepository.saveAll(sportReservations);
		sportItemCart.clear();

		return "redirect:/orders";
	}

	@GetMapping("/orders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var plotReservations = plotReservationRepository.findByUserId(user.getId());
		model.addAttribute("plotOrdersCompleted", plotReservations);
		var sportReservations = sportItemReservationRepository.findByUserId(user.getId());
		model.addAttribute("sportOrdersCompleted", sportReservations);
		var seasonalReservations = seasonalPlotReservationRepository.findByUserId(user.getId());
		model.addAttribute("seasonalOrdersCompleted", seasonalReservations);
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

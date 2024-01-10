package campingplatz.reservation;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotCart;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import campingplatz.reservation.Reservation;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({ "plotCart", "SportItemCart", "seasonalCart"})
@EnableScheduling
class ReservationController {

	private final PlotReservationRepository plotReservationRepository;
	private final SportItemReservationRepository sportItemReservationRepository;
	private final SeasonalPlotReservationRepository seasonalPlotReservationRepository;

	ReservationController(PlotReservationRepository plotReservationRepository,
			SportItemReservationRepository sportItemReservationRepository,
			SeasonalPlotReservationRepository seasonalPlotReservationRepository) {

		this.plotReservationRepository = plotReservationRepository;
		this.sportItemReservationRepository = sportItemReservationRepository;
		this.seasonalPlotReservationRepository = seasonalPlotReservationRepository;
	}

	@ModelAttribute("plotCart")
	PlotCart initializeCart() {
		return new PlotCart();
	}

	@ModelAttribute("SportItemCart")
	SportItemCart initializeSportCart() {
		return new SportItemCart();
	}

	@ModelAttribute("seasonalCart")
    SeasonalPlotCart initializeSeasonalCart() {
        return new SeasonalPlotCart();
    }

	@GetMapping("/cart")
	String cart(Model model, @LoggedIn UserAccount userAccount,
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("seasonalCart") SeasonalPlotCart seasonalPlotCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		var plotReservations = reservationCart.getReservationsOfUser(userAccount);
		model.addAttribute("plotReservations", plotReservations);

		var sportsReservations = sportItemCart.getReservationsOfUser(userAccount);
		model.addAttribute("sportsReservations", sportsReservations);
		
		var seasonalReservations = seasonalPlotCart.getReservationsOfUser(userAccount);
		model.addAttribute("seasonalReservations", seasonalReservations);

		var total = reservationCart.getPrice().add(sportItemCart.getPrice());
		model.addAttribute("total", total);

		return "servings/cart";
	}

	@PostMapping("/cart/remove/{type}/{index}")
	String removeCartItem(Model model, @LoggedIn UserAccount userAccount, @PathVariable("index") int index, @PathVariable("type") String type, 
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		if (type.equals("PlotReservation")){

			var cart = reservationCart.getReservationsOfUser(userAccount);
			var item = cart.get(index - 1);
			reservationCart.remove(item);
		}
		else if (type.equals("SportItemReservation")){

			var cart = sportItemCart.getReservationsOfUser(userAccount);
			var item = cart.get(index - 1);
			sportItemCart.remove(item);
		}

		
		return "redirect:/cart";
	}

	@PostMapping("/checkout")
	String reservate(Model model, @LoggedIn UserAccount userAccount,
			@ModelAttribute("plotCart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

		List<PlotReservation> reservations = reservationCart.getReservationsOfUser(userAccount);
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

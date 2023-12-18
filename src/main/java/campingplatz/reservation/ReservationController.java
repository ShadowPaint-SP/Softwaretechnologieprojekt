package campingplatz.reservation;

import campingplatz.equip.sportsitemreservations.SportItemCart;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.equip.sportsitemreservations.SportItemReservationRepository;
import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;

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
@SessionAttributes({"plotCart", "SportItemCart"})
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

		var allPlotReservations = reservationCart.getReservationsOfUser(userAccount);
		var validPlotReservation = validatePlotReservations(allPlotReservations);
		plotReservationRepository.saveAll(validPlotReservation);
		reservationCart.clear();

		var allSportReservations = sportItemCart.getReservationsOfUser(userAccount);
		var validSportReservation = validateSportItemReservations(allSportReservations);
		sportItemReservationRepository.saveAll(validSportReservation);
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




	public List<PlotReservation> validatePlotReservations(List<PlotReservation> input) {
		var validStream = input.stream().filter(reservation -> {
			// end should not be before begin
			if (reservation.getEnd().isBefore(reservation.getBegin())){
				return false;
			}

			// begin should not be before date.now()
			if (reservation.getBegin().plusDays(1).isBefore(LocalDateTime.now())){
				return false;
			}

			// should not have overlaps with existing reserved
			return plotReservationRepository.productIsAvailableIn(
				reservation.getProduct(),
				reservation.getBegin(),
				reservation.getEnd()
			);
		});
		return validStream.toList();
	}

	public List<SportItemReservation> validateSportItemReservations(List<SportItemReservation> input) {
		var validStream = input.stream().filter(reservation -> {
			// end should not be before begin
			if (reservation.getEnd().isBefore(reservation.getBegin())){
				return false;
			}

			// begin should not be before date.now()
			if (reservation.getBegin().plusDays(1).isBefore(LocalDateTime.now())){
				return false;
			}

			return true;
		});
		return validStream.toList();
	}



}

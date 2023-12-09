package campingplatz.seasonalplots;

import campingplatz.reservation.ReservationRepository;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;
import java.time.Month;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
@EnableScheduling
public class SeasonalPlotReservationController {
	private final ReservationRepository<SeasonalPlot> reservationRepository;

	public SeasonalPlotReservationController(ReservationRepository<SeasonalPlot> reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@PostMapping("/seasonalcheckout")
	String reservate(Model model, @LoggedIn UserAccount userAccount, SeasonalPlot seasonalPlot, SeasonalPlotReservation.PayMethod payMethod) {

		SeasonalPlotReservation reservation = new SeasonalPlotReservation(userAccount, seasonalPlot,
			LocalDateTime.now(), null, payMethod);
		reservationRepository.save(reservation);

		return "redirect:/orders";
	}

	@GetMapping("/orders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		model.addAttribute("ordersCompleted", userReservations);
		return "servings/orders";
	}
}

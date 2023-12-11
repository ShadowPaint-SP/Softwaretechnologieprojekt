package campingplatz.reservation;

import campingplatz.equip.sportsItemReservations.SportItemCart;
import campingplatz.equip.sportsItemReservations.SportItemReservation;
import campingplatz.equip.sportsItemReservations.SportItemReservationRepository;
import campingplatz.plots.Plot;
import campingplatz.plots.plotReservations.PlotCart;
import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.plots.plotReservations.PlotReservationRepository;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({"cart", "SportItemCart"})
@EnableScheduling
class ReservationController {

    private final PlotReservationRepository plotReservationRepository;
	private final SportItemReservationRepository sportItemReservationRepository;

    ReservationController(PlotReservationRepository plotReservationRepository,
						  SportItemReservationRepository sportItemReservationRepository) {

        this.plotReservationRepository = plotReservationRepository;
		this.sportItemReservationRepository = sportItemReservationRepository;
    }



    @ModelAttribute("cart")
	PlotCart initializePlotCart() {
        return new PlotCart();
    }

	@ModelAttribute("SportItemCart")
	SportItemCart initializeSportCart() {
		return new SportItemCart();
	}




    @GetMapping("/cart")
    String cart(Model model, @LoggedIn UserAccount userAccount,
				@ModelAttribute("cart") PlotCart reservationCart,
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
            @ModelAttribute("cart") PlotCart reservationCart,
			@ModelAttribute("SportItemCart") SportItemCart sportItemCart) {

        List<PlotReservation> plotReservations = reservationCart.getReservationsOfUser(userAccount);
		plotReservationRepository.saveAll(plotReservations);
        reservationCart.clear();

		List<SportItemReservation> sportReservations = sportItemCart.getReservationsOfUser(userAccount);
		sportItemReservationRepository.saveAll(sportReservations);
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
	// were we are deleting the reservations older than the current day if they were not taken
	@Scheduled(cron = "0 00 10 * * ?")
	public void periodicallyDeleteReservatinos() {
		plotReservationRepository.deleteBeforeThan(LocalDateTime.now());
	}
}

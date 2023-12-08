package campingplatz.reservation;

import campingplatz.plots.Plot;
import campingplatz.utils.Cart;
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
@SessionAttributes("cart")
@EnableScheduling
class ReservationController {

    private final ReservationRepository<Plot> reservationRepository;

    ReservationController(ReservationRepository<Plot> reservationRepository) {

        this.reservationRepository = reservationRepository;
    }

    @ModelAttribute("cart")
    Cart<Plot> initializeCart() {
        return new Cart<Plot>(PlotReservation.class);
    }

    @GetMapping("/cart")
    String cart(Model model, @LoggedIn UserAccount userAccount, @ModelAttribute("cart") Cart<Plot> reservationCart) {
        var reservations = reservationCart.getReservationsOfUser(userAccount);
        model.addAttribute("reservations", reservations);
        return "servings/cart";
    }

    @PostMapping("/checkout")
    String reservate(Model model, @LoggedIn UserAccount userAccount,
            @ModelAttribute("cart") Cart<Plot> reservationCart) {

        List<Reservation<Plot>> reservations = reservationCart.getReservationsOfUser(userAccount);
        reservationRepository.saveAll(reservations);
        reservationCart.clear();

        return "redirect:/";
    }

    @GetMapping("/orders")
    String orders(Model model, @LoggedIn UserAccount user) {
        var userReservations = reservationRepository.findByUserId(user.getId());
        model.addAttribute("ordersCompleted", userReservations);
        return "servings/orders";
    }



	// we are scheduling a task to be executed at 10:00 AM on the 15th day of every month.
	// were we are deleting the reservations older than the current day if they were not taken
	@Scheduled(cron = "0 00 10 * * ?")
	public void periodicallyDeleteReservatinos() {
		reservationRepository.deleteBeforeThan(LocalDateTime.now());
	}
}

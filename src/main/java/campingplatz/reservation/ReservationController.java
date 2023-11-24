package campingplatz.reservation;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
class ReservationController {

    private final ReservationRepository reservations;

    ReservationController(ReservationRepository reservations) {

        this.reservations = reservations;
    }

    @ModelAttribute("cart")
    ReservationCart initializeCart() {
        return new ReservationCart();
    }

    @GetMapping("/cart")
    String cart(Model model) {
        return "cart";
    }

    @PostMapping("/checkout")
    String reservate(Model model, @LoggedIn UserAccount userAccount,
            @ModelAttribute("cart") ReservationCart reservationCart) {

        reservations.saveAll(reservationCart);
        reservationCart.clear();

        return "redirect:/";
    }

    @PostMapping("updateTimePeriod")
    String updateArrival(Model model, @ModelAttribute("cart") ReservationCart reservationCart,
            @RequestParam("id") UUID reservationID,
            @RequestParam("arrival") LocalDate arrival, @RequestParam("departure") LocalDate departure) {

        for (Reservation product : reservationCart) {
            if (product.getId().equals(reservationID)) {
                product.setArrival(arrival);
                product.setDeparture(departure);
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/orders")
    String orders(Model model, @LoggedIn UserAccount user) {
        var userReservations = reservations.findByUserId(user.getId());
        model.addAttribute("ordersCompleted", userReservations);
        return "orders";
    }
}

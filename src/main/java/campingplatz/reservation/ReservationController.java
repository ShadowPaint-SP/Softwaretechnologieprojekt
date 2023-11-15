/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package campingplatz.reservation;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalog;
import campingplatz.utils.Cart;

import jakarta.validation.Valid;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    Cart<Reservation> initializeCart() {
        return new Cart<>();
    }

    @PostMapping("/cart/{plot}")
    String addReservation(Model model, @LoggedIn UserAccount user, @PathVariable Plot plot, @Valid PlotCatalog.SiteState state, @ModelAttribute("cart") Cart<Reservation> cart) {

        var reservation = new Reservation(user, plot, state.getArrival(), state.getDeparture());
        cart.add(reservation);

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    String cart(Model model) {
        return "cart";
    }

    @PostMapping("/checkout")
    String reservate( Model model, @LoggedIn UserAccount userAccount, @ModelAttribute("cart") Cart<Reservation> cart ) {

        reservations.saveAll(cart);
        cart.clear();

        return "redirect:/";
    }

    @PostMapping("updateTimePeriod")
    String updateArrival( Model model, @ModelAttribute("cart") Cart<Reservation> cart, @RequestParam("id") UUID reservationID,
		 @RequestParam("arrival") LocalDate arrival, @RequestParam("departure") LocalDate departure
	){

        for (Reservation product : cart) {
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

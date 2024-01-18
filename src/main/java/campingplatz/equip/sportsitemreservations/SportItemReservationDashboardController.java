package campingplatz.equip.sportsitemreservations;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

/**
 * Controller handling operations related to the management of sport item reservations.
 */
@Controller
public class SportItemReservationDashboardController {

	private SportItemReservationRepository sportItemReservations;

	public SportItemReservationDashboardController(SportItemReservationRepository sportItemReservations) {
		this.sportItemReservations = sportItemReservations;
	}


    /**
     * Retrieves and displays all sport item reservations for management purposes.
     *
     * @param model The Spring MVC model for passing data to the view.
     * @return The view name for the sport item reservations management dashboard.
     */
	@GetMapping("/management/sportitems/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model) {
		List<SportItemReservation> rer = sportItemReservations.findAll();
		model.addAttribute("reservationsSportItem", rer);
		return "dashboards/reservation_sportitems_management";
	}


    /**
     * Handles the return of a sport item and updates the reservation status.
     *
     * @param information The information about the sport item reservation change.
     * @return Redirects to the sport item reservations management dashboard.
     */

    @PostMapping("/management/sportitems/return")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String returnSportItem(@Valid SportItemReservationChangeInformation information) {

		UUID id = information.getReservationUUID();
		SportItemReservation reservation = sportItemReservations.findById(id).get();

		sportItemReservations.delete(reservation);

		return "redirect:/management/sportitems/reservation";
	}


    /**
     * Interface representing information about a change in sport item reservation.
     */
	interface SportItemReservationChangeInformation {
		UUID getReservationUUID();

		Integer getStateValue();

	}

}
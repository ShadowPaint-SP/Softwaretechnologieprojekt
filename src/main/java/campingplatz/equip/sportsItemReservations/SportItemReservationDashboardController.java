package campingplatz.equip.sportsItemReservations;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SportItemReservationDashboardController {

	SportItemReservationRepository sportItemReservations;

	SportItemReservationDashboardController(SportItemReservationRepository sportItemReservations) {
		this.sportItemReservations = sportItemReservations;
	}

	@GetMapping("/management/sportitems/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model) {
		List<SportItemReservation> rer = sportItemReservations.findAll();
		model.addAttribute("reservationsSportItem", rer);
		return "dashboards/reservation_sportitems_management";
	}


}
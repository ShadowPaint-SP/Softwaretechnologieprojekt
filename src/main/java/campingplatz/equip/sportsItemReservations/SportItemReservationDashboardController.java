package campingplatz.equip.sportsItemReservations;

import campingplatz.equip.SportItem;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class SportItemReservationDashboardController {

	private SportItemReservationRepository sportItemReservations;

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

	@PostMapping("/management/sportitems/return")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String returnSportItem(@Valid SportItemReservationChangeInformation information) {

		UUID id = information.getReservationUUID();

		SportItemReservation reservation = sportItemReservations.findById(id).get();

		SportItem item = reservation.getProduct();
		item.setAmount(item.getAmount() + 1);

		sportItemReservations.delete(reservation);

		return "redirect:/management/sportitems/reservation";
	}

	interface SportItemReservationChangeInformation {
		UUID getReservationUUID();

		Integer getStateValue();

	}


}
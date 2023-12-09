package campingplatz.reservation;

import campingplatz.plots.Plot;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@SessionAttributes("cart")
public class ReservationDashboardController {

	ReservationRepository<Plot> plotReservations;

	ReservationDashboardController(ReservationRepository<Plot> plotReservations) {
		this.plotReservations = plotReservations;
	}

	@GetMapping("/management/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model) {
		List<Reservation<Plot>> all = plotReservations.findAll();
		model.addAttribute("reservations", all);
		return "dashboards/reservation_management";
	}

	@PostMapping("/management/reservation/updateReservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model, @Valid ReservationChangeInformation info) {

		var uuid = info.getReservationUUID();
		var reservation = plotReservations.findById(uuid).get();
		var state = Reservation.State.fromNumber(info.getStateValue());

		// update and save
		reservation.setState(state);
		plotReservations.save(reservation);

		List<Reservation<Plot>> all = plotReservations.findAll();
		model.addAttribute("reservations", all);
		return "dashboards/reservation_management";
	}

	interface ReservationChangeInformation {
		UUID getReservationUUID();

		Integer getStateValue();

	}

}

package campingplatz.reservation;

import campingplatz.plots.Plot;
import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.plots.plotReservations.PlotReservationRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class ReservationDashboardController {

	PlotReservationRepository plotReservations;

	ReservationDashboardController(PlotReservationRepository plotReservations) {
		this.plotReservations = plotReservations;
	}

	@GetMapping("/management/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model) {
		List<PlotReservation> all = plotReservations.findAll();
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

		List<PlotReservation> all = plotReservations.findAll();
		model.addAttribute("reservations", all);
		return "dashboards/reservation_management";
	}

	interface ReservationChangeInformation {
		UUID getReservationUUID();

		Integer getStateValue();

	}

}

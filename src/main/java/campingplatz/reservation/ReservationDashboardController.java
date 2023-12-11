package campingplatz.reservation;

import campingplatz.plots.Plot;
import campingplatz.plots.plotReservations.PlotReservation;
import campingplatz.plots.plotReservations.PlotReservationRepository;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
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
@SessionAttributes("plotCart")
public class ReservationDashboardController {

	PlotReservationRepository plotReservations;
	SeasonalPlotReservationRepository seasonalPlotReservations;


	ReservationDashboardController(PlotReservationRepository plotReservations, SeasonalPlotReservationRepository seasonalPlotReservations) {
		this.plotReservations = plotReservations;
		this.seasonalPlotReservations = seasonalPlotReservations;
	}

	@GetMapping("/management/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model) {
		List<PlotReservation> all = plotReservations.findAll();
		List<SeasonalPlotReservation> allS = seasonalPlotReservations.findAll();

		model.addAttribute("reservations", all);
		model.addAttribute("seasonalreservations", allS);
		return "dashboards/reservation_management";
	}

	@PostMapping("/management/reservation/updateReservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String updateSeasonal(Model model, @Valid ReservationChangeInformation info) {

		var uuid = info.getReservationUUID();
		var reservation = plotReservations.findById(uuid).get();
		var state = Reservation.State.fromNumber(info.getStateValue());

		// update and save
		reservation.setState(state);
		plotReservations.save(reservation);

		// List<PlotReservation> all = plotReservations.findAll();
		// model.addAttribute("reservations", all);
		return "redirect:/management/reservation";
	}

	@PostMapping("/management/reservation/updateSeasonalReservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String customer(Model model, @Valid ReservationChangeInformation info) {

		var uuid = info.getReservationUUID();
		var reservation = seasonalPlotReservations.findById(uuid).get();
		var state = Reservation.State.fromNumber(info.getStateValue());

		// update and save
		reservation.setState(state);
		seasonalPlotReservations.save(reservation);

		return "redirect:/management/reservation";
	}

	interface ReservationChangeInformation {
		UUID getReservationUUID();

		Integer getStateValue();

	}

}

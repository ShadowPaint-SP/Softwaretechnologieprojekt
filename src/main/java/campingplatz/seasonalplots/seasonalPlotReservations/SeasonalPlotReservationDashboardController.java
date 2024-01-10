package campingplatz.seasonalplots.seasonalPlotReservations;

import java.util.List;
import java.util.UUID;

import org.salespointframework.accountancy.Accountancy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import campingplatz.reservation.Reservation;
import jakarta.validation.Valid;

@Controller
public class SeasonalPlotReservationDashboardController {

	SeasonalPlotReservationRepository plotReservations;
	Accountancy accountancy;

	SeasonalPlotReservationDashboardController(
			SeasonalPlotReservationRepository plotReservations,
			Accountancy accountancy) {
		this.plotReservations = plotReservations;
		this.accountancy = accountancy;
	}

	@GetMapping("/management/seasonalreservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String reservations(Model model) {
		List<SeasonalPlotReservation> all = plotReservations.findAll();

		model.addAttribute("reservations", all);
		return "dashboards/reservation_seasonalPlot_management";
	}

	@PostMapping("/management/seasonalreservation/updateReservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String updateReservations(Model model, @Valid ReservationChangeInformation info) {

		var uuid = info.getReservationUUID();
		var reservation = plotReservations.findById(uuid).get();
		// var oldState = reservation.getState();

		var newState = Reservation.State.fromNumber(info.getStateValue());
		var costsMeter = reservation.getTotalPrice(info.getNewElectricityMeter(), info.getNewWaterMeter());

		// update and save
		reservation.setState(newState);
		plotReservations.save(reservation);

		List<SeasonalPlotReservation> all = plotReservations.findAll();
		model.addAttribute("reservations", all);
		return "redirect:/management/seasonalreservation";
	}


	interface ReservationChangeInformation {
		UUID getReservationUUID();
		Integer getStateValue();
		Double getNewElectricityMeter();
		Double getNewWaterMeter();
	}

}

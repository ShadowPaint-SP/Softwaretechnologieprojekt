package campingplatz.plots.plotReservations;

import campingplatz.accounting.PlotReservationAccountancyEntry;
import campingplatz.accounting.PlotReservationDeductionEntry;
import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import jakarta.validation.Valid;
import org.salespointframework.accountancy.Accountancy;
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
public class PlotReservationDashboardController {

	PlotReservationRepository plotReservations;
	Accountancy accountancy;

	PlotReservationDashboardController(PlotReservationRepository plotReservations,
		Accountancy accountancy) {
		this.plotReservations = plotReservations;
		this.accountancy = accountancy;
	}

	@GetMapping("/management/reservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String reservations(Model model) {
		List<PlotReservation> all = plotReservations.findAll();

		model.addAttribute("reservations", all);
		return "dashboards/reservation_management";
	}

	@PostMapping("/management/reservation/updateReservation")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String updateReservations(Model model, @Valid ReservationChangeInformation info) {

		var uuid = info.getReservationUUID();
		var reservation = plotReservations.findById(uuid).get();
		var oldState = reservation.getState();

		var newState = Reservation.State.fromNumber(info.getStateValue());


		if (oldState == Reservation.State.PAYED && newState != Reservation.State.PAYED){
			var entry = new PlotReservationDeductionEntry(reservation);
			accountancy.add(entry);
		}
		if (oldState != Reservation.State.PAYED && newState == Reservation.State.PAYED){
			var entry = new PlotReservationAccountancyEntry(reservation);
			accountancy.add(entry);
		}



		// update and save
		reservation.setState(newState);
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

package campingplatz.seasonalplots.seasonalPlotReservations;

import java.util.List;
import java.util.UUID;

import org.salespointframework.accountancy.Accountancy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import campingplatz.accounting.SeasonalPlotReservationAccountancyEntry;
import campingplatz.accounting.SeasonalPlotReservationDeductionEntry;
import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.SeasonalPlotCatalog;
import jakarta.validation.Valid;

@Controller
public class SeasonalPlotReservationDashboardController {

	SeasonalPlotReservationRepository plotReservations;
	Accountancy accountancy;
	SeasonalPlotCatalog seasonalPlotCatalog;

	SeasonalPlotReservationDashboardController(
			SeasonalPlotReservationRepository plotReservations,
			Accountancy accountancy, SeasonalPlotCatalog plotCatalog) {
		this.plotReservations = plotReservations;
		this.accountancy = accountancy;
		this.seasonalPlotCatalog = plotCatalog;
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
		var plot = reservation.getProduct();
		try{ // is null if reservation is payed
			reservation.setElectricityDifference(info.getNewElectricityMeter());
			reservation.setWaterDifference(info.getNewWaterMeter());
		} catch (Exception e){}
		seasonalPlotCatalog.save(plot);

		var oldState = reservation.getState();

		var newState = Reservation.State.fromNumber(info.getStateValue());
		var costsMeter = reservation.getPrice();


		if (oldState == Reservation.State.PAYED && newState != Reservation.State.PAYED) {
			var entry = new SeasonalPlotReservationDeductionEntry(reservation);
			accountancy.add(entry);
		}
		if (oldState != Reservation.State.PAYED && newState == Reservation.State.PAYED) {
			var entry = new SeasonalPlotReservationAccountancyEntry(reservation);
			accountancy.add(entry);
		}

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

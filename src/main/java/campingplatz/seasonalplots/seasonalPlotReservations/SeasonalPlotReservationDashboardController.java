package campingplatz.seasonalplots.seasonalPlotReservations;

import java.util.List;
import java.util.UUID;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.BusinessTime.DayHasPassed;
import org.salespointframework.time.BusinessTime.MonthHasPassed;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
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
	BusinessTime businessTime;

	SeasonalPlotReservationDashboardController(
			SeasonalPlotReservationRepository plotReservations,
			BusinessTime businessTime,
			Accountancy accountancy, SeasonalPlotCatalog plotCatalog) {
		this.plotReservations = plotReservations;
		this.accountancy = accountancy;
		this.seasonalPlotCatalog = plotCatalog;
		this.businessTime = businessTime;
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
		try { // is null if reservation is payed
			reservation.setElectricityDifference(info.getNewElectricityMeter());
			reservation.setWaterDifference(info.getNewWaterMeter());
		} catch (Exception e) {
		}
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

	public void dayHasPassed() {
		System.out.println("Day has passed");

		List<SeasonalPlotReservation> all = plotReservations.findAll();
		for (var plot : all) {
			plot.updateMonthlyPayment(businessTime.getTime());
			plotReservations.save(plot);
		}

	}

	@Component
	class TimedEventListener {

		@EventListener
		void on(DayHasPassed event) {
			dayHasPassed();
			// Code to be triggered daily goes here
		}

		@EventListener
		void on(MonthHasPassed event) {
			// Code to be triggered monthly goes here
		}
	}
}

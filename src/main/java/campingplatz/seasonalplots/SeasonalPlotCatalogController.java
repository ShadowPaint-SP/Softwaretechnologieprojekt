package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotDashboardController;
import campingplatz.reservation.Reservation;
import campingplatz.reservation.ReservationRepository;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotCart;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	SeasonalPlotReservationRepository reservationRepository;

	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog,
			SeasonalPlotReservationRepository reservationRepository) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
	}


	@GetMapping("/seasonalplotcatalog")
	String setupSeasonalCatalog(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query) {
		var filteredSeasonalPlots = seasonalPlotCatalog.seasonalFilter(query);
		var reservedSeasonalPlots = reservationRepository.findPlotsAll();
		var freeSeasonalPlots = filteredSeasonalPlots.stream().collect(Collectors.partitioningBy(
				seasonalPlot -> !reservedSeasonalPlots.contains(seasonalPlot)));

				if (user.isPresent()) {
					var userReservations = reservationRepository.findByUserId(user.get().getId());
					model.addAttribute("ordersCompleted", userReservations);
				}

				model.addAttribute("allSeasonalPlots", freeSeasonalPlots);
				model.addAttribute("searchQuery", query);
				
		return "servings/seasonalplotcatalog";
	}

	@PostMapping("/seasonalplotcatalog/filter")
	String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid SeasonalPlotCatalog.SeasonalSiteState query) {
		return setupSeasonalCatalog(model, user, query);
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalcheckout/{plot}")
	String reservate(Model model, @LoggedIn UserAccount userAccount,
					 Integer payMethod, @PathVariable("plot") SeasonalPlot seasonalPlot) {

		// seasonal Plots are offered from April to October
		// the reservation will start on the next possible date
		var inApril = LocalDateTime.now().withMonth(4).withDayOfMonth(1);
		int monthNow = LocalDateTime.now().getMonthValue();
		// take next year if the season is over
		if (monthNow >= 10) {
			inApril = inApril.plusYears(1);
		} else if (monthNow > 4)  {
			inApril = LocalDateTime.now();
		}
		var inOctober = inApril.withMonth(10).withDayOfMonth(31);
		SeasonalPlotReservation reservation = new SeasonalPlotReservation(userAccount, seasonalPlot,
		inApril, inOctober, SeasonalPlotReservation.PayMethod.fromNumberPayMethod(payMethod));
		reservationRepository.save(reservation);
		

		return "redirect:/seasonalplotcatalog";
	}

	@GetMapping("/seasonalorders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		for(SeasonalPlotReservation reservation : userReservations) {
			reservation.setNextYear(reservation.isNextYear());
		}
		model.addAttribute("ordersCompleted", userReservations);
		return "servings/orders";
	}

	/*
	@PostMapping("/seasonalplotnavaible")
	String nextYearAvaible(Model model, @LoggedIn UserAccount user) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		for(SeasonalPlotReservation reservation : userReservations) {
			if(!reservation.isNextYearAvaible()) {
				userReservations.remove(reservation);
			}
		}
		model.addAttribute("nextYearAvaiblePlot", userReservations);
		return "redirect:/seasonalplotcatalog";
	}*/

}

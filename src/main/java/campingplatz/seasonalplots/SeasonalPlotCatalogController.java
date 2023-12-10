package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotDashboardController;
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
	String reservate(Model model, @LoggedIn UserAccount userAccount, @PathVariable("plot") SeasonalPlot seasonalPlot,
			SeasonalPlotReservation.PayMethod payMethod) {

		SeasonalPlotReservation reservation = new SeasonalPlotReservation(userAccount, seasonalPlot,
		LocalDateTime.now(), null, payMethod);
		reservationRepository.save(reservation);
		

		return "redirect:/seasonalplotcatalog";
	}

	@GetMapping("/seasonalorders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		model.addAttribute("ordersCompleted", userReservations);
		return "servings/orders";
	}

}

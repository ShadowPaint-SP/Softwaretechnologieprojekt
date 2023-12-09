package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalog;
import campingplatz.reservation.ReservationRepository;
import campingplatz.utils.Cart;
import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	ReservationRepository<SeasonalPlot> reservationRepository;

	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog, ReservationRepository<SeasonalPlot> reservationRepository) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
	}

	@GetMapping("/seasonalplotcatalog")
	String setupSeasonalCatalog(Model model, @LoggedIn Optional<UserAccount> user, @Valid  SeasonalPlotCatalog.SeasonalSiteState query) {
		var filteredSeasonalPlots = seasonalPlotCatalog.seasonalFilter(query);
		var reservedSeasonalPlots = reservationRepository.findAll();
		var freeSeasonalPlots = filteredSeasonalPlots.stream().collect(Collectors.partitioningBy(
			seasonalPlot -> !reservedSeasonalPlots.contains(seasonalPlot)));

		model.addAttribute("allSeasonalPlots", freeSeasonalPlots);
		model.addAttribute("searchQuery", query);

		return "old/seasonalplotcatalog";
	}

	@PostMapping("/seasonalplotcatalog/filter")
	String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid SeasonalPlotCatalog.SeasonalSiteState query) {
		return setupSeasonalCatalog(model, user, query);
	}

	//TODO Dashboard verweis
}

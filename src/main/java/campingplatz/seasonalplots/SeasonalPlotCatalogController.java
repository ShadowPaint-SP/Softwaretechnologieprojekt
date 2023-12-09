package campingplatz.seasonalplots;

import campingplatz.reservation.ReservationRepository;
import campingplatz.utils.Cart;
import jakarta.validation.Valid;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	ReservationRepository<SeasonalPlot> reservationRepository;

	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog,
			ReservationRepository<SeasonalPlot> reservationRepository) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
	}

	@ModelAttribute("cart")
	Cart<SeasonalPlot> initializeCart() {
		return new Cart<SeasonalPlot>(SeasonalPlotReservation.class);
	}

	@GetMapping("/seasonalplotcatalog")
	String setupSeasonalCatalog(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query, @ModelAttribute("cart") Cart<SeasonalPlot> reservationCart) {
		var filteredSeasonalPlots = seasonalPlotCatalog.seasonalFilter(query);
		var reservedSeasonalPlots = reservationRepository.findPlotsAll();
		var freeSeasonalPlots = filteredSeasonalPlots.stream().collect(Collectors.partitioningBy(
				seasonalPlot -> !reservedSeasonalPlots.contains(seasonalPlot)));

		model.addAttribute("allSeasonalPlots", freeSeasonalPlots);
		model.addAttribute("searchQuery", query);

		return "servings/seasonalplotcatalog";
	}

	@PostMapping("/seasonalplotcatalog/filter")
	String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid SeasonalPlotCatalog.SeasonalSiteState query,
			@ModelAttribute("cart") Cart<SeasonalPlot> reservationCart) {
		return setupSeasonalCatalog(model, user, query, reservationCart);
	}

	@PostMapping("/seasonalcheckout/{plot}")
	String reservate(Model model, @LoggedIn UserAccount userAccount, @PathVariable("plot") SeasonalPlot seasonalPlot,
			SeasonalPlotReservation.PayMethod payMethod) {

		SeasonalPlotReservation reservation = new SeasonalPlotReservation(userAccount, seasonalPlot,
				LocalDateTime.now(), null, payMethod);
		reservationRepository.save(reservation);

		return "redirect:/cart";
	}

	@GetMapping("/seasonalorders")
	String orders(Model model, @LoggedIn UserAccount user) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		model.addAttribute("ordersCompleted", userReservations);
		return "servings/orders";
	}
}

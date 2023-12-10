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
@SessionAttributes("cart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	SeasonalPlotReservationRepository reservationRepository;

	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog,
			SeasonalPlotReservationRepository reservationRepository) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
	}

	@ModelAttribute("cart")
	SeasonalPlotCart initializeCart() {
		return new SeasonalPlotCart();
	}

	@GetMapping("/seasonalplotcatalog")
	String setupSeasonalCatalog(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query,
			@ModelAttribute("cart") SeasonalPlotCart reservationCart) {
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
	String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid SeasonalPlotCatalog.SeasonalSiteState query,
			@ModelAttribute("cart") SeasonalPlotCart reservationCart) {
		return setupSeasonalCatalog(model, user, query, reservationCart);
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



	@GetMapping("/management/seasonalplot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String seasonalPlots(Model model) {
		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	@PostMapping("/management/seasonalplot/updateSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String changeSeasonalPlotDetails(Model model, @Valid SeasonalPlotCatalogController.SeasonalPlotInformation info) {

		Optional<SeasonalPlot> plotOptional = seasonalPlotCatalog.findById(info.getPlotID());
		if (plotOptional.isPresent()) {
			SeasonalPlot plot = plotOptional.get();
			plot.setName(info.getName());
			plot.setSize(info.getSize());
			plot.setParking(Plot.ParkingLot.fromNumber(info.getParkingValue()));
			plot.setPrice(Money.of(info.getPrice(), EURO));
			plot.setElectricityMeter(info.getElectricityMeter());
			plot.setWaterMeter(info.getWaterMeter());
			plot.setImagePath(info.getPicture());
			plot.setDesc(info.getDescription());

			// dont forget to save
			seasonalPlotCatalog.save(plot);

			Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
			model.addAttribute("seasonalPlots", all);
		}

		return "redirect:/management/seasonalplot";
	}

	@PostMapping("/management/seasonalplot/createSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String createSeasonalPlot(Model model, @Valid SeasonalPlotCatalogController.SeasonalPlotInformation info) {

		var splot = new SeasonalPlot(
			info.getName(),
			info.getSize(),
			Money.of(info.getPrice(), EURO),
			Plot.ParkingLot.fromNumber(info.getParkingValue()),
			info.getElectricityMeter(),
			info.getWaterMeter(),
			info.getPicture(),
			info.getDescription());

		// dont forget to save
		seasonalPlotCatalog.save(splot);

		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	// TODO
	@PostMapping("/management/seasonalplot/deleteSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String deleteSeasonalPlot(Model model, @Valid SeasonalPlotCatalogController.SeasonalPlotInformation info) {

		// cannot just delete the entry, reservations might depend on it
		try {
			seasonalPlotCatalog.deleteById(info.getPlotID());
		} catch (Exception e) {
			// just continue
		}

		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	interface SeasonalPlotInformation {

		Product.ProductIdentifier getPlotID();

		String getName();

		Double getSize();

		Integer getParkingValue();

		Double getElectricityMeter();

		Double getWaterMeter();

		Double getPrice();

		String getDescription();

		String getPicture();

		Plot.Condition.State getState();
	}
}

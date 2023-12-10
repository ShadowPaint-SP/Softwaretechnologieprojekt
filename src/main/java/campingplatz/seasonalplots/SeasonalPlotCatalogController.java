package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.reservation.ReservationRepository;
import campingplatz.utils.Cart;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
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
			@Valid SeasonalPlotCatalog.SeasonalSiteState query,
			@ModelAttribute("cart") Cart<SeasonalPlot> reservationCart) {
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
			@ModelAttribute("cart") Cart<SeasonalPlot> reservationCart) {
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
	@PreAuthorize("hasRole('BOSS')")
	public String managementSetup(Model model) {

		List<SeasonalPlot> allSPLots = seasonalPlotCatalog.findAll().stream().toList();
		model.addAttribute("allSPlots", allSPLots);



		return "dashboards/seasonalplot_management";
	}

	@PostMapping("/addSeasonalPlot")
	@PreAuthorize("hasRole('Boss')")
	public String addSeasonalPlot(@RequestParam String name,
			  @RequestParam Double size,
			  @RequestParam Money price,
			  @RequestParam Plot.ParkingLot parkingLot,
			  @RequestParam double electricityMeter,
			  @RequestParam double waterMeter,
			  @RequestParam String imagePath,
			  @RequestParam String description) {

		SeasonalPlot newSeasonalPlot = seasonalPlotCatalog.findByName(name).stream().findFirst().orElse(null);
		if(newSeasonalPlot == null) {
			seasonalPlotCatalog.save(new SeasonalPlot(name,
				size, price, parkingLot, electricityMeter,
				waterMeter, imagePath, description));
		} else {
			newSeasonalPlot.setName(name);
			newSeasonalPlot.setSize(size);
			newSeasonalPlot.setPrice(price);
			newSeasonalPlot.setParking(parkingLot);
			newSeasonalPlot.setElectricityMeter(electricityMeter);
			newSeasonalPlot.setWaterMeter(waterMeter);
			newSeasonalPlot.setImagePath(imagePath);
			newSeasonalPlot.setDesc(description);
		}
		return "redirect: /management/seasonalplot";
	}

	//TODO Controller für defekte Stellplätze, Funktion hat Vincent

	@PostMapping("/deleteSeasonalPlot")
	@PreAuthorize("hasRole('BOSS')")
	public String deleteSeasonalPlot(@RequestParam String name,
			 @RequestParam(required = false)Product.ProductIdentifier id) {
		SeasonalPlot oldSeasonalPlot = seasonalPlotCatalog.findByName(name).stream().findFirst().orElse(null);
		if (oldSeasonalPlot != null && oldSeasonalPlot.getId() != null) {
			seasonalPlotCatalog.deleteById(oldSeasonalPlot.getId());
		}
		return "redirect: /management/seasonalplot";
	}

	@PostMapping("/changeElectricityPrice")
	@PreAuthorize("hasRole('BOSS')")
	public String changeElectricityPrice(@RequestParam double newElectricityCosts) {
		Config.setElectricityCosts(Money.of(newElectricityCosts, EURO));
		return "redirect: /management/seasonalplot";
	}

	@PostMapping("/changeWaterPrice")
	@PreAuthorize("hasRole('BOSS')")
	public String changeWaterPrice(@RequestParam double newWaterCosts) {
		Config.setWaterCosts(Money.of(newWaterCosts, EURO));
		return "redirect: /management/seasonalplot";
	}

}

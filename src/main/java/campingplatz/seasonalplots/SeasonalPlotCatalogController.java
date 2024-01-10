package campingplatz.seasonalplots;

import campingplatz.plots.plotreservations.PlotCart;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotCart;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import campingplatz.utils.Comment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("seasonalCart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	SeasonalPlotReservationRepository reservationRepository;
	BusinessTime businessTime;
	
	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog,
			SeasonalPlotReservationRepository reservationRepository, BusinessTime businessTime) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
		this.businessTime = businessTime;
			}

	@ModelAttribute("seasonalCart")
    SeasonalPlotCart initializeSeasonalCart() {
        return new SeasonalPlotCart();
    }

	@GetMapping("/seasonalplotcatalog")
	String setupSeasonalCatalog(Model model, @LoggedIn Optional<UserAccount> user,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query) {
		var filteredSeasonalPlots = seasonalPlotCatalog.seasonalFilter(query);
		var reservedSeasonalPlots = reservationRepository.findPlotsAll();
		var freeSeasonalPlots = filteredSeasonalPlots.stream().collect(Collectors.partitioningBy(
				seasonalPlot -> !reservedSeasonalPlots.contains(seasonalPlot)));
		var available = new ArrayList<SeasonalPlotReservation>();

		if (user.isPresent()) {
			var userReservations = reservationRepository.findByUserId(user.get().getId());
			for (SeasonalPlotReservation reservation : userReservations) {
				if (businessTime.getTime().isAfter(reservation.getEnd().plusYears(1).withMonth(3).withMonth(3))) {
					if (reservation.isShow()) {
						freeSeasonalPlots.get(true).add(reservation.getProduct());
					}
					reservation.setShow(false);
				}
				if (reservation.isShow()) {
					available.add(reservation);
				}
			}
		}
		model.addAttribute("ordersCompleted", available);
		model.addAttribute("allSeasonalPlots", freeSeasonalPlots);
		model.addAttribute("searchQuery", query);
		model.addAttribute("currentDate", businessTime.getTime());

		return "servings/seasonalplotcatalog";
	}

	@PostMapping("/seasonalplotcatalog/filter")
	String filter(Model model, @LoggedIn Optional<UserAccount> user, @Valid SeasonalPlotCatalog.SeasonalSiteState query) {
		return setupSeasonalCatalog(model, user, query);
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalcheckout/{plot}")
	String reservate(Model model, @LoggedIn UserAccount user, @PathVariable("plot") SeasonalPlot seasonalPlot,
			Integer payMethod, SeasonalPlotCart seasonalPlotCart) {

		var inApril = businessTime.getTime().withMonth(4).withDayOfMonth(1);
		int monthNow = businessTime.getTime().getMonthValue();
		// take next year if the season is over
		if (monthNow >= 10) {
			inApril = inApril.plusYears(1);
		} else if (monthNow > 4) {
			inApril = businessTime.getTime();
		}
		var userReservations = reservationRepository.findByUserId(user.getId());
		for (SeasonalPlotReservation reservation : userReservations) {
			if (reservation.getProduct().getId() == seasonalPlot.getId()) {
				reservation.setShow(false);
			}

		}
		var inOctober = inApril.withMonth(10).withDayOfMonth(31);
		SeasonalPlotReservation reservation = new SeasonalPlotReservation(user, seasonalPlot,
				inApril, inOctober, SeasonalPlotReservation.PayMethod.fromNumberPayMethod(payMethod));
		// reservationRepository.save(reservation);


        seasonalPlotCart.add(reservation);


		return "redirect:/cart";
	}

	@PostMapping("/updateseasonalplot/{plot}")
	String updateSeasonalPlot(Model model, @LoggedIn UserAccount user,
			@PathVariable("plot") SeasonalPlotReservation seasonalplotreservation) {
		var userReservations = reservationRepository.findByUserId(user.getId());
		for (SeasonalPlotReservation reservation : userReservations) {
			if (reservation.getId() == seasonalplotreservation.getId()) {
				var inApril = SeasonalPlot.getArrival(businessTime.getTime());
				var inOctober = inApril.withMonth(10).withDayOfMonth(31);
				reservation.setBegin(inApril);
				reservation.setEnd(inOctober);
			}
		}
		return "redirect:/seasonalplotcatalog";
	}

	@GetMapping("/seasonalplotcatalog/details/{plot}")
	public String showSeasonalPlotDetails(Model model,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query, @PathVariable("plot") SeasonalPlot plot) {
		model.addAttribute("item", plot);
		return "servings/seasonalplotdetails";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalplotcatalog/details/{plot}/comments")
	public String seasonalComment(Model model, @PathVariable("plot") SeasonalPlot plot, CommentInfo info) {
		plot.addComment(new Comment(info.getComment(), info.getRating(), businessTime.getTime()));
		seasonalPlotCatalog.save(plot);
		return "redirect:/seasonalplotcatalog/details/" + plot.getId();
	}

	interface CommentInfo {
		@NotEmpty
		String getComment();

		@Range(min = 1, max = 5)
		int getRating();
	}

	@GetMapping("/forward/{days}")
	String forwardTime(Model model, @PathVariable("days") int days) {
		businessTime.forward(Duration.ofDays(days));

		return "redirect:/seasonalplotcatalog";
	}
}

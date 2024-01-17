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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("seasonalCart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	SeasonalPlotReservationRepository reservationRepository;
	Set<SeasonalPlotReservation> activReservationRepository;
	BusinessTime businessTime;

	public SeasonalPlotCatalogController(SeasonalPlotCatalog seasonalPlotCatalog,
			SeasonalPlotReservationRepository reservationRepository, BusinessTime businessTime) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
		this.reservationRepository = reservationRepository;
		this.activReservationRepository = new HashSet<>();
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
		var activeReservedSeasonalPlots = new HashSet<SeasonalPlot>();
		for (SeasonalPlotReservation activeReservation : activReservationRepository) {
			activeReservedSeasonalPlots.add(activeReservation.getProduct());
		}
		var freeSeasonalPlots = filteredSeasonalPlots.stream().collect(Collectors.partitioningBy(
				seasonalPlot -> !activeReservedSeasonalPlots.contains(seasonalPlot)));
		var myOrders = new ArrayList<SeasonalPlotReservation>();

		var active = new HashSet<>(activReservationRepository);
		for (SeasonalPlotReservation activeReservation : active) {
			if (businessTime.getTime().isAfter(activeReservation.getEnd().plusYears(1).withMonth(3).withMonth(3))) {
				activReservationRepository.remove(activeReservation);
				freeSeasonalPlots.get(true).add(activeReservation.getProduct());
					 } else {
				try {
					if (activeReservation.getUser().getId().equals(user.get().getId()) && reservationRepository.existsById(activeReservation.getId())) {
						myOrders.add(activeReservation);
					}
				} catch (Exception e) {
				}
			}
		}

		model.addAttribute("ordersCompleted", myOrders);
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
			Integer payMethod, @ModelAttribute("seasonalCart") SeasonalPlotCart seasonalPlotCart) {

		var inApril = businessTime.getTime().withMonth(4).withDayOfMonth(1);
		int monthNow = businessTime.getTime().getMonthValue();
		// take next year if the season is over
		if (monthNow >= 10) {
			inApril = inApril.plusYears(1);
		} else if (monthNow > 4) {
			inApril = businessTime.getTime();
		}

		var inOctober = inApril.withMonth(10).withDayOfMonth(31);
		SeasonalPlotReservation reservation = new SeasonalPlotReservation(user, seasonalPlot,
				inApril, inOctober, SeasonalPlotReservation.PayMethod.fromNumberPayMethod(payMethod));

		var active = new HashSet<>(activReservationRepository);
		for (SeasonalPlotReservation activeReservation : active) {
			if (activeReservation.getProduct().equals(seasonalPlot))
				activReservationRepository.remove(activeReservation);
		}

		activReservationRepository.add(reservation);
		seasonalPlotCart.add(reservation);
		
		return "redirect:/cart";
	}
	

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalcancel/{plot}")
	String cancel(Model model, @LoggedIn UserAccount user, @PathVariable("plot") SeasonalPlot seasonalPlot) {

		for (SeasonalPlotReservation reservation : activReservationRepository) {
			if (reservation.getProduct().getId().equals(seasonalPlot.getId()) && reservation.getUser().equals(user)) {
				activReservationRepository.remove(reservation);
			}
		}
		return "redirect:/seasonalplotcatalog";
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
	public String seasonalComment(Model model, @PathVariable("plot") SeasonalPlot plot, CommentInfo info, @LoggedIn UserAccount currUserAccount) {
        Set<UserAccount> commentarySet=reservationRepository.findUsersOfProduct(plot);
        if(commentarySet.contains(currUserAccount)){
            plot.addComment(new Comment(info.getComment(), info.getRating(), businessTime.getTime(),currUserAccount.getFirstname(), currUserAccount.getLastname()));
            seasonalPlotCatalog.save(plot);
        return "redirect:/plotcatalog/details/" + plot.getId();
        }else{
            model.addAttribute("error", true);
            boolean error = true;
            seasonalPlotCatalog.save(plot);
            model.addAttribute("item", plot);

			return "servings/seasonalplotdetails";
        }
	}

	interface CommentInfo {
		@NotEmpty
		String getComment();

		@Range(min = 1, max = 5)
		int getRating();
	}


}

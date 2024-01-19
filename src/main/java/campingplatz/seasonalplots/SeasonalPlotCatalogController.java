package campingplatz.seasonalplots;

import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotCart;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import campingplatz.plots.Comment;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("seasonalCart")
public class SeasonalPlotCatalogController {
	SeasonalPlotCatalog seasonalPlotCatalog;
	SeasonalPlotReservationRepository reservationRepository;
    /**
     * Contains all user reservations that have not yet been completed
     */
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

    /**
     * Shows all plots that are available and
     * shows the user all his current reservations.
     *
     * @param user  actual logged user
     * @param query filter data
     */
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

        //checks whether the active reservations have already expired or need to be updated
		var active = new HashSet<>(activReservationRepository);
		for (SeasonalPlotReservation activeReservation : active) {
			if (businessTime.getTime().isAfter(activeReservation.getEnd().plusYears(1).withMonth(3).withMonth(3))) {
				activReservationRepository.remove(activeReservation);
				freeSeasonalPlots.get(true).add(activeReservation.getProduct());
			} else {
				try {
					if (activeReservation.getUser().getId().equals(user.get().getId())
							&& reservationRepository.existsById(activeReservation.getId())) {
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

    /**
     * Saves a reservation with the first possible start date and ends on 31.10.
     *
     * @param user              actual logged user
     * @param seasonalPlot      plot to be reserved
     * @param payMethod         user paid monthly or yearly
     * @param seasonalPlotCart  plot get in cart
     */
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

        //set end of period to 31.10.
		var inOctober = inApril.withMonth(10).withDayOfMonth(31);
		SeasonalPlotReservation reservation = new SeasonalPlotReservation(user, seasonalPlot,
				inApril, inOctober, SeasonalPlotReservation.PayMethod.fromNumberPayMethod(payMethod));

        //saves all active Reservation in one HashSet
		var active = new HashSet<>(activReservationRepository);
		for (SeasonalPlotReservation activeReservation : active) {
			if (activeReservation.getProduct().equals(seasonalPlot))
				activReservationRepository.remove(activeReservation);
		}

		activReservationRepository.add(reservation);
		seasonalPlotCart.add(reservation);

		return "redirect:/cart";
	}

    /**
     * User can delete a plot if he does not want
     * to reserve it again for the next season.
     *
     * @param user          actual logged user
     * @param seasonalPlot  plot to be unsubscribed
     */
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalcancel/{plot}")
	String cancel(Model model, @LoggedIn UserAccount user, @PathVariable("plot") SeasonalPlot seasonalPlot) {

        for (SeasonalPlotReservation reservation : activReservationRepository) {
            if (Objects.equals(reservation.getProduct().getId(), seasonalPlot.getId()) && reservation.getUser().equals(user)) {
                activReservationRepository.remove(reservation);
                break;
            }
        }
		return "redirect:/seasonalplotcatalog";
	}

    /*
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
	}*/

    /**
     * Shows the details from a plot with comments
     *
     * @param plot  plot from which the details are to be seen
     */
	@GetMapping("/seasonalplotcatalog/details/{plot}")
	public String showSeasonalPlotDetails(Model model,
			@Valid SeasonalPlotCatalog.SeasonalSiteState query, @PathVariable("plot") SeasonalPlot plot) {
		model.addAttribute("item", plot);
		return "servings/seasonalplotdetails";
	}

    /**
     * A comment can be added to the plot.
     *
     * @param plot              plot that get a comment
     * @param info              rating and text for the comment
     * @param currUserAccount   actual logged user
     */
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/seasonalplotcatalog/details/{plot}/comments")
	public String seasonalComment(Model model, @PathVariable("plot") SeasonalPlot plot, CommentInfo info,
			@LoggedIn UserAccount currUserAccount) {
		Set<UserAccount> commentarySet = reservationRepository.findUsersOfProduct(plot);
		if (commentarySet.contains(currUserAccount)) {
			plot.addComment(new Comment(info.getComment(), info.getRating(), businessTime.getTime(),
					currUserAccount.getFirstname(), currUserAccount.getLastname()));
			seasonalPlotCatalog.save(plot);
			return "redirect:/plotcatalog/details/" + plot.getId();
		} else {
			model.addAttribute("error", true);
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

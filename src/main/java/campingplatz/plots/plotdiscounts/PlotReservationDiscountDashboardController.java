package campingplatz.plots.plotdiscounts;


import campingplatz.plots.plotreservations.PlotReservation;
import jakarta.validation.Valid;
import org.salespointframework.catalog.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class PlotReservationDiscountDashboardController {

	private PlotReservationDiscountRepository plotReservationDiscounts;

	public PlotReservationDiscountDashboardController(PlotReservationDiscountRepository plotReservationDiscounts){
		this.plotReservationDiscounts = plotReservationDiscounts;
	}


	@GetMapping("/management/discount")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String discount(Model model) {
		List<PlotReservationDiscount> all = plotReservationDiscounts.findAll();

		model.addAttribute("discounts", all);
		return "dashboards/discount_management";
	}

	interface DiscountInformation {
		UUID getUUID();
		Integer getAmount();
		Integer getDiscount();
	}


	@PostMapping("/management/discount")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String changeDiscount(Model model, @Valid DiscountInformation info) {
		var discount = plotReservationDiscounts.findById(info.getUUID()).get();

		discount.setAmount(info.getAmount());
		discount.setDiscountPercent(info.getDiscount());

		plotReservationDiscounts.save(discount);

		return discount(model);
	}





}

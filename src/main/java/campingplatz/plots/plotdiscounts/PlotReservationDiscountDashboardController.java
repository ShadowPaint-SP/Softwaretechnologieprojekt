package campingplatz.plots.plotdiscounts;

import jakarta.validation.Valid;
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

	public PlotReservationDiscountDashboardController(PlotReservationDiscountRepository plotReservationDiscounts) {
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

        Integer getOldAmount();

		Integer getAmount();

		Integer getDiscount();
	}

	@PostMapping("/management/discount/changeDiscount")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String changeDiscount(Model model, @Valid DiscountInformation info) {

        var changedDiscount = plotReservationDiscounts.findById(info.getUUID()).get();

        var preexisting = plotReservationDiscounts.findAllByAmount(info.getAmount());
        for (var discount : preexisting){
            if (discount != changedDiscount){
                plotReservationDiscounts.deleteById(discount.getId());
            }

        }

        changedDiscount.setAmount(info.getAmount());
        changedDiscount.setDiscountPercent(info.getDiscount());

        plotReservationDiscounts.save(changedDiscount);
        return discount(model);
	}

	@PostMapping("/management/discount/createDiscount")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String createDiscount(Model model, @Valid DiscountInformation info) {

		var preexisting = plotReservationDiscounts.findAllByAmount(info.getAmount());
        for (var discount : preexisting){
            plotReservationDiscounts.deleteById(discount.getId());
        }

        var discount = new PlotReservationDiscount();
        discount.setAmount(info.getAmount());
		discount.setDiscountPercent(info.getDiscount());

		plotReservationDiscounts.save(discount);
		return discount(model);
	}


	@PostMapping("/management/discount/deleteDiscount")
	@PreAuthorize("hasAnyRole('EMPLOYEE', 'BOSS')")
	String deleteDiscount(Model model, @Valid DiscountInformation info) {

		plotReservationDiscounts.deleteById(info.getUUID());

		return discount(model);
	}

}

package campingplatz.accounting;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import campingplatz.plots.plotreservations.PlotReservationRepository;

@Controller
public class AccountancyDashboardController {

	Accountancy accountancy;

	AccountancyDashboardController(Accountancy accountancy) {
		this.accountancy = accountancy;
	}

	@GetMapping("/management/accountancy")
	@PreAuthorize("hasAnyRole('BOSS')")
	String accountyncy(Model model) {
		Streamable<AccountancyEntry> all = accountancy.findAll();
		model.addAttribute("accountancy", all);
		return "dashboards/accountancy_management";
	}

}

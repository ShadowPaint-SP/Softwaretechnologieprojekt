package campingplatz.accounting;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
		CurrencyUnit euro = Monetary.getCurrency("EUR");
		MonetaryAmount total = all.stream()
				.map(AccountancyEntry::getValue)
				.reduce(MonetaryAmount::add)
				.orElse(Monetary.getDefaultAmountFactory().setCurrency(euro).setNumber(0).create());
		model.addAttribute("total", total);
		model.addAttribute("accountancy", all);
		return "dashboards/accountancy_management";
	}

}

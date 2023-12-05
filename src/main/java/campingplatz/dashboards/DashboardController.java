package campingplatz.dashboards;

import campingplatz.customer.Customer;
import campingplatz.customer.CustomerManagement;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DashboardController
 */
@Controller
public class DashboardController {

	CustomerManagement customerManagement;

	DashboardController(CustomerManagement customerManagement) {
		this.customerManagement = customerManagement;
	}
	@GetMapping("/management/customer")
	String customer(Model model) {
		Streamable<Customer> all = customerManagement.findAll();
		model.addAttribute("Customers", all);
		return "dashboards/customer_mamangement";
	}

	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasRole('BOSS')")
	String sportsequipment(Model model) {
		return "dashboards/sportsequipment_mamangement";
	}

}
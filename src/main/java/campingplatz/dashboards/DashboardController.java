package campingplatz.dashboards;

import campingplatz.customer.Customer;
import campingplatz.customer.CustomerManagement;
import jakarta.validation.Valid;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

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
	@PreAuthorize("hasRole('BOSS')")
	String customer(Model model) {
		Streamable<Customer> all = customerManagement.findAll();
		model.addAttribute("Customers", all);
		return "dashboards/customer_mamangement";
	}

	@PostMapping("/management/customer/updateRoles")
	@PreAuthorize("hasRole('BOSS')")
	String changeRole(Model model, @Valid RoleChangeInformation info) {

		var uuid = info.getCustomerUUID();
		var customer = customerManagement.findByUUID(uuid).get();
		var role = Customer.Roles.fromNumber(info.getRole());

		customer.setRole(role);

		Streamable<Customer> all = customerManagement.findAll();
		model.addAttribute("Customers", all);
		return "dashboards/customer_mamangement";
	}

	interface RoleChangeInformation {
		UUID getCustomerUUID();

		Integer getRole();

	}

	@GetMapping("/management/sportsequipment")
	@PreAuthorize("hasRole('BOSS')")
	String sportsequipment(Model model) {
		return "dashboards/sportsequipment_mamangement";
	}

}
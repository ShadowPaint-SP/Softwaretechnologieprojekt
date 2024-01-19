package campingplatz.customer;

import jakarta.validation.Valid;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.UUID;

/**
 * This class is responsible for handling requests related to the customer
 * dashboard.
 * It provides functionalities for managing customers, updating roles, and
 * displaying customer data.
 */
@Controller
@SessionAttributes("plotCart")
public class CustomerDashboardController {

	CustomerManagement customerManagement;

	/**
	 * Constructs a new instance of {@link CustomerDashboardController}.
	 *
	 * @param customerManagement the {@link CustomerManagement} object used for
	 *                           managing customer data.
	 */
	CustomerDashboardController(CustomerManagement customerManagement) {
		this.customerManagement = customerManagement;
	}

	/**
	 * Handles GET requests for the customer management page.
	 *
	 * @param model the {@link Model} object used for passing attributes to the
	 *              view.
	 * @return the name of the view to be rendered.
	 */
	@GetMapping("/management/customer")
	@PreAuthorize("hasRole('BOSS')")
	String customer(Model model) {
		Streamable<Customer> all = customerManagement.findAll();
		model.addAttribute("Customers", all);
		return "dashboards/customer_management";
	}

	/**
	 * Handles POST requests for updating customer roles.
	 *
	 * @param model the {@link Model} object used for passing attributes to the
	 *              view.
	 * @param info  the {@link RoleChangeInformation} object containing the
	 *              necessary information for role update.
	 * @return the name of the view to be rendered.
	 */
	@PostMapping("/management/customer/updateRoles")
	@PreAuthorize("hasRole('BOSS')")
	String changeRole(Model model, @Valid RoleChangeInformation info) {

		var uuid = info.getCustomerUUID();
		var customer = customerManagement.findByUUID(uuid).get();
		var role = Customer.Roles.fromNumber(info.getRoleValue());

		customer.setRole(role);

		Streamable<Customer> all = customerManagement.findAll();
		model.addAttribute("Customers", all);
		return "dashboards/customer_management";
	}

	/**
	 * An interface representing the necessary information for changing a customer's
	 * role.
	 */
	interface RoleChangeInformation {
		UUID getCustomerUUID();

		Integer getRoleValue();

	}

}
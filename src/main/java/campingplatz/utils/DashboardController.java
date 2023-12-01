package campingplatz.utils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DashboardController
 */
@Controller
public class DashboardController {

	// @GetMapping("/management/customer")
	// String customer(Model model) {
	// return "dashboards/customer_mamangement";
	// }

	@GetMapping("/management/sportsequipment")
	String sportsequipment(Model model) {
		return "dashboards/sportsequipment_mamangement";
	}

}
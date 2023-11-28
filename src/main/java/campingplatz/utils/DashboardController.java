package campingplatz.utils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * DashboardController
 */
@Controller
public class DashboardController {

	@GetMapping("/dashboard")
	String dashboard(Model model) {
		return "contents/dashboard";
	}

}
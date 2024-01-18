package campingplatz.utils;

import java.time.Duration;

import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller class for handling business time operations in the campingplatz
 * management system.
 * It provides methods for forwarding the business time by a certain number of
 * days.
 */
@Controller
public class BusinessTimeController {

    private BusinessTime businessTime;

    /**
     * Constructor for the BusinessTimeController class.
     * Initializes the BusinessTime instance.
     *
     * @param businessTime the BusinessTime instance to be managed by this
     *                     controller
     */
    public BusinessTimeController(BusinessTime businessTime) {
        this.businessTime = businessTime;
    }

    /**
     * Handles GET requests to forward the business time by a certain number of
     * days.
     * The number of days is extracted from the URL path variable.
     * After forwarding the business time, it redirects to the requested URL.
     *
     * @param model   the Model object to hold attributes for the view
     * @param days    the number of days to forward the business time
     * @param request the HttpServletRequest object containing the client's request
     * @return a string representing the redirect URL
     */
    @GetMapping("/forward/{days}/**")
    String fTime(Model model, @PathVariable("days") int days, HttpServletRequest request) {
        var uri = request.getRequestURI();
        var url = uri.replace("/forward/" + days, "");
        if (url.equals("")) {
            url = "/";
        }

        businessTime.forward(Duration.ofDays(days));

        return "redirect:" + url;
    }

}

package campingplatz.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BusinessTimeController {
    
    private BusinessTime businessTime;


    public BusinessTimeController(BusinessTime businessTime) {
        this.businessTime = businessTime;
    }




    @GetMapping("/f/{days}/**")
	String fTime(Model model, @PathVariable("days") int days, HttpServletRequest request) {
		var uri =  request.getRequestURI();
		var url = uri.replace("/f/" + days, "");

		businessTime.forward(Duration.ofDays(days));

		return "redirect:" + url;
	}

    @GetMapping("/**")
	String forwardTime(Model model, HttpServletRequest request) {
		var uri =  request.getRequestURI();
        var contains = uri.split("/forward/");
		var url = contains[0];
        try {
            int days = Integer.parseInt(contains[1]);
            businessTime.forward(Duration.ofDays(days));
        } catch (Exception e) {}


		return "redirect:" + url;
	}
}

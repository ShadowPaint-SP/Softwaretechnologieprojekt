package campingplatz.utils;

import java.time.Duration;

import org.salespointframework.time.BusinessTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BusinessTimeController {
    
    private BusinessTime businessTime;


    public BusinessTimeController(BusinessTime businessTime) {
        this.businessTime = businessTime;
    }




    @GetMapping("/forward/{days}/**")
	String fTime(Model model, @PathVariable("days") int days, HttpServletRequest request) {
		var uri =  request.getRequestURI();
		var url = uri.replace("/forward/" + days, "");
        if (url.equals("")){
            url = "/";
        }

		businessTime.forward(Duration.ofDays(days));

		return "redirect:" + url;
	}


}

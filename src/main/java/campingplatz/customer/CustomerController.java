package campingplatz.customer;

import jakarta.validation.Valid;

import org.salespointframework.useraccount.Password;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This class handles the registration and login process for customers.
 */
@Controller
@SessionAttributes("plotCart")
class CustomerController {

    private final CustomerManagement customerManagement;

    /**
     * Constructor for the CustomerController class.
     *
     * @param customerManagement The service layer for handling operations related
     *                           to customers.
     */
    CustomerController(CustomerManagement customerManagement) {
        Assert.notNull(customerManagement, "CustomerManagement must not be null!");
        this.customerManagement = customerManagement;
    }

    /**
     * Handles the POST request for the "/register" endpoint.
     * Creates a new customer with the provided form data.
     *
     * @param form   The form data containing the customer's details.
     * @param result The errors object to hold validation errors.
     * @param model  The model object to pass attributes to the view.
     * @return The name of the view to render.
     */
    @PostMapping("/register")
    String registerNew(@Valid RegistrationForm form, Errors result, Model model) {
        try {
            customerManagement.create(form.getEmail(),
                    Password.UnencryptedPassword.of(form.getPassword()),
                    Customer.Roles.CUSTOMER.getValue(),
                    form.getName(),
                    form.getLast());
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "User with this email already exists!");
            return "static/register";
        }

        return "redirect:/default/login";
    }

    /**
     * Handles the GET request for the "/register" endpoint.
     * Renders the registration page.
     *
     * @param model The model object to pass attributes to the view.
     * @param form  The form object to bind form data.
     * @return The name of the view to render.
     */
    @GetMapping("/register")
    String register(Model model, RegistrationForm form) {
        return "static/register";
    }

    /**
     * Handles the GET request for the "/default/login" endpoint.
     * Redirects to the default login page.
     *
     * @param model The model object to pass attributes to the view.
     * @return The name of the view to render.
     */
    @GetMapping("/default/login")
    public String faildloginredirect(Model model) {
        return "static/defaultlogin";
    }

}

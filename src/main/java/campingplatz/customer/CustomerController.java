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

@Controller
@SessionAttributes("cart")
class CustomerController {

    private final CustomerManagement customerManagement;

    CustomerController(CustomerManagement customerManagement) {

        Assert.notNull(customerManagement, "CustomerManagement must not be null!");

        this.customerManagement = customerManagement;
    }

    @PostMapping("/register")
    String registerNew(@Valid RegistrationForm form, Errors result, Model model) {

        // boolean checkbox1 = form.isDauercamper();
        // form.validate(result);

        // if (result.hasErrors()) {
        // return "static/register";
        // }
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

        //// Falls alles in Ordnung ist legen wir einen Customer an
        // customerManagement.create(form.getEmail(),
        // Password.UnencryptedPassword.of(form.getPassword()),
        // Customer.Roles.CUSTOMER.getValue(),
        // form.getName(),
        // form.getLast());

        return "redirect:/default/login";
    }

    @GetMapping("/register")
    String register(Model model, RegistrationForm form) {
        return "static/register";
    }

    @GetMapping("/default/login")
    public String faildloginredirect(Model model) {
        return "static/defaultlogin";
    }

}

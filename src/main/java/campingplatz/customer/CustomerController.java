package campingplatz.customer;

import jakarta.validation.Valid;

import org.salespointframework.useraccount.Password;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class CustomerController {

    private final CustomerManagement customerManagement;

    CustomerController(CustomerManagement customerManagement) {

        Assert.notNull(customerManagement, "CustomerManagement must not be null!");

        this.customerManagement = customerManagement;
    }

    @PostMapping("/register")
    String registerNew(@Valid RegistrationForm form, Errors result) {

        // boolean checkbox1 = form.isDauercamper();
        // form.validate(result);

        if (result.hasErrors()) {
            return "static/register";
        }

        // Falls alles in Ordnung ist legen wir einen Customer an
        customerManagement.create(form.getName(), Password.UnencryptedPassword.of(form.getPassword()), Customer.Roles.CUSTOMER.getValue());

        return "redirect:/";
    }

    @GetMapping("/register")
    String register(Model model, RegistrationForm form) {
        return "static/register";
    }

    @GetMapping("/management/customer")
    String customer(Model model) {
        Streamable<Customer> all = customerManagement.findAll();
        model.addAttribute("Customers", all);
        return "dashboards/customer_mamangement";
    }
}

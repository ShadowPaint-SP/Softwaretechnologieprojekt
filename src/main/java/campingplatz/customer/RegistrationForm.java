package campingplatz.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import org.springframework.validation.Errors;

/**
 * This class represents a registration form for customers.
 * It encapsulates the data required for a customer to register, including their
 * name, last name, password, and email.
 */
class RegistrationForm {

    /**
     * The first name of the customer.
     * The last name of the customer.
     * The password chosen by the customer for their account.
     * The email address of the customer.
     */
    @Getter
    private final @NotEmpty String name, last, password, email;

    /**
     * Constructs a new RegistrationForm object with the given name, last name,
     * password, and email.
     *
     * @param name     The first name of the customer.
     * @param last     The last name of the customer.
     * @param password The password chosen by the customer for their account.
     * @param email    The email address of the customer.
     */
    public RegistrationForm(String name, String last, String password, String email) {
        this.name = name;
        this.last = last;
        this.password = password;
        this.email = email;
    }

    /**
     * Validates the registration form data.
     *
     * @param errors An Errors object to store validation errors.
     */
    public void validate(Errors errors) {
        if (name.equals("") && last.equals("") && password.equals("")) {
            errors.rejectValue("password", "Benutzername und Passwort dürfen nicht leer sein.");
        }

    }
}

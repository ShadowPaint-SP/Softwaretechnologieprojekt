package campingplatz.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import org.springframework.validation.Errors;

class RegistrationForm {
    @Getter
    private final @NotEmpty String name, last, password, email;

    public RegistrationForm(String name, String last, String password, String email) {

        this.name = name;
        this.last = last;
        this.password = password;
        this.email = email;
    }

    public void validate(Errors errors) {
        if (name.equals("") && last.equals("") && password.equals("")) {
            errors.rejectValue("password", "Benutzername und Passwort dürfen nicht leer sein.");
        }

    }
}

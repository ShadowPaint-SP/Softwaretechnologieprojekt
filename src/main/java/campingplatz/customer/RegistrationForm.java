package campingplatz.customer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import org.springframework.validation.Errors;

class RegistrationForm {
    @Getter
    private final @NotEmpty String name, last, password;

    public RegistrationForm(String name, String last, String password) {

        this.name = name;
        this.last = last;
        this.password = password;
    }

    public String getFullName() {
        return name + " " + last;
    }

    public void validate(Errors errors) {
        if (name.equals("") && last.equals("") && password.equals("")) {
            errors.rejectValue("password", "Benutzername und Passwort dürfen nicht leer sein.");
        }

    }
}

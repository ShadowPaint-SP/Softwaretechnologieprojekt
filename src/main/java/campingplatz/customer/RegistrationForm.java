package campingplatz.customer;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.Errors;

class RegistrationForm {

    private final @NotEmpty String name, password;

    public RegistrationForm(String name, String password) {

        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    
    public void validate(Errors errors) {
        if (name.equals("")&&password.equals("")) {
			errors.rejectValue("password", "Benutzername und Passwort dürfen nicht leer sein.");
		}

    }
}

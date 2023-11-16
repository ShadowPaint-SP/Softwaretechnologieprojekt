package campingplatz.customer;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.Errors;

class RegistrationForm {

    private final @NotEmpty String name, password, address;
    private @NotEmpty String dauercamper;

    public RegistrationForm(String name, String password, String address, String dauercamper) {

        this.name = name;
        this.password = password;
        this.address = address;
        this.dauercamper = dauercamper;

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String isDauercamper() {
        return dauercamper;
    }

    public void setDauercamper(String dauercamper_neu) {
        this.dauercamper = dauercamper_neu;

    }

    public void validate(Errors errors) {
        // Complex validation goes here
    }
}

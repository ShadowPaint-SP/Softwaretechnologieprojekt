
package campingplatz.customer;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationFormUnitTest {

    @Test
    void testConstructorAndGetters() {
        
        String name = "Max";
        String last = "Musterman";
        String password = "123";
        String email = "maxmusterman@mail.de";

        RegistrationForm form = new RegistrationForm(name, last, password, email);

        assertEquals(name, form.getName());
        assertEquals(last, form.getLast());
        assertEquals(password, form.getPassword());
    }

    @Test
    void testValidate() {
        
        String validName = "Max";
        String validLast = "Musterman";
        String validPassword = "123";
        String validEmail = "maxmusterman@mail.de";
        RegistrationForm validForm = new RegistrationForm(validName, validLast, validPassword, validEmail);

        String invalidName = "";
        String invalidLast = "";
        String invalidPassword = "";
        String invalidEmail = "";
        RegistrationForm invalidForm = new RegistrationForm(invalidName, invalidLast, invalidPassword, invalidEmail);

        Errors validErrors = new BeanPropertyBindingResult(validForm, "validForm");
        Errors invalidErrors = new BeanPropertyBindingResult(invalidForm, "invalidForm");

        validForm.validate(validErrors);
        invalidForm.validate(invalidErrors);

        assertFalse(validErrors.hasErrors());

        assertTrue(invalidErrors.hasErrors());
    }
}


package campingplatz.customer;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationFormUnitTest {

    @Test
    void testConstructorAndGetters() {

        String name = "Max";
        String password = "123";

        RegistrationForm form = new RegistrationForm(name, password);

        assertEquals(name, form.getName());
        assertEquals(password, form.getPassword());
    }

    @Test
    void testValidate() {

        String validName = "Max";
        String validPassword = "123";
        RegistrationForm validForm = new RegistrationForm(validName, validPassword);

        String invalidName = "";
        String invalidPassword = "";
        RegistrationForm invalidForm = new RegistrationForm(invalidName, invalidPassword);

        Errors validErrors = new BeanPropertyBindingResult(validForm, "validForm");
        Errors invalidErrors = new BeanPropertyBindingResult(invalidForm, "invalidForm");

        validForm.validate(validErrors);
        invalidForm.validate(invalidErrors);

        assertFalse(validErrors.hasErrors());

        assertTrue(invalidErrors.hasErrors());
    }
}

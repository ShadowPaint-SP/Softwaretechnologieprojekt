package campingplatz.customer;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;


@Component
@Order(10)
class CustomerDataInitializer implements DataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDataInitializer.class);

    private final UserAccountManagement userAccountManagement;
    private final CustomerManagement customerManagement;

    /**
     * Creates a new {@link CustomerDataInitializer} with the given
     * {@link UserAccountManagement} and
     * {@link CustomerRepository}.
     *
     * @param userAccountManagement must not be {@literal null}.
     * @param customerManagement    must not be {@literal null}.
     */
    CustomerDataInitializer(UserAccountManagement userAccountManagement, CustomerManagement customerManagement) {

        Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
        Assert.notNull(customerManagement, "CustomerRepository must not be null!");

        this.userAccountManagement = userAccountManagement;
        this.customerManagement = customerManagement;
    }


    @Override
    public void initialize() {

        // Skip creation if database was already populated
        if (userAccountManagement.findByUsername("boss").isPresent()) {
            return;
        }

        LOG.info("Creating default users and customers.");

        userAccountManagement.create("boss", UnencryptedPassword.of("123"), Role.of("BOSS"));

        var password = "123";
		// TODO: realistische und viel wichtiger eigene Daten initialisieren
        // TODO: brauchen wir noch das dauercamper Attribut?
        List.of(//
                new RegistrationForm("hans", password, "wurst", ""),
                new RegistrationForm("dextermorgan", password, "Miami-Dade County", ""),
                new RegistrationForm("earlhickey", password, "Camden County - Motel", ""),
                new RegistrationForm("mclovinfogell", password, "Los Angeles", ""))
                .forEach(customerManagement::createCustomer);
    }
}

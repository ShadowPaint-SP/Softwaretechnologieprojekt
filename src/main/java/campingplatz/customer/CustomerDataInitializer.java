package campingplatz.customer;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

                var password = "123";

                customerManagement.create("meister@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.EMPLOYEE.getValue(),
                                "alter", "meister");
                customerManagement.create("boss@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.BOSS.getValue(),
                                "boss", "boss");
                customerManagement.create("hans@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.CUSTOMER.getValue(),
                                "hans", "wurst");
                customerManagement.create("jürgen@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.CUSTOMER.getValue(),
                                "jürgen", "fridrig");
                customerManagement.create("westphal@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.EMPLOYEE.getValue(),
                                "west", "phal");
                customerManagement.create("dextermorgan@mail.de", UnencryptedPassword.of(password),
                                Customer.Roles.CUSTOMER.getValue(),
                                "dexter", "morgan");

        }
}

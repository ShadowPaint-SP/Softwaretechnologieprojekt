package campingplatz.customer;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class CustomerManagement {

    public static final Role CUSTOMER_ROLE_DC = Role.of("CUSTOMER");

    private final CustomerRepository customers;
    private final UserAccountManagement userAccounts;

    /**
     * Creates a new {@link CustomerManagement} with the given
     * {@link CustomerRepository} and
     * {@link UserAccountManagement}.
     *
     * @param customers    must not be {@literal null}.
     * @param userAccounts must not be {@literal null}.
     */
    CustomerManagement(CustomerRepository customers, UserAccountManagement userAccounts) {

        Assert.notNull(customers, "CustomerRepository must not be null!");
        Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

        this.customers = customers;
        this.userAccounts = userAccounts;
    }

    /**
     * Creates a new {@link Customer} using the information given in the
     * {@link RegistrationForm}.
     *
     * @param form must not be {@literal null}.
     * @return the new {@link Customer} instance.
     */
    public Customer createCustomer(RegistrationForm form) {

        Assert.notNull(form, "Registration form must not be null!");

        var password = UnencryptedPassword.of(form.getPassword());

        var userAccount = userAccounts.create(form.getName(), password, CUSTOMER_ROLE_DC);
        return customers.save(new Customer(userAccount));

        

    }

    /**
     * Returns all {@link Customer}s currently available in the system.
     *
     * @return all {@link Customer} entities.
     */
    public Streamable<Customer> findAll() {
        return customers.findAll();
    }
}

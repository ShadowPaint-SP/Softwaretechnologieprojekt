package campingplatz.customer;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CustomerManagement {

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
    public Customer create(String email, UnencryptedPassword password, Role roles, String firstname, String lastname) {
        // create an account
		var userAccount = userAccounts.create(email, password, roles);
		userAccount.setFirstname(firstname);
		userAccount.setLastname(lastname);

		// dont forget to save
		var modifiedAccount = userAccounts.save(userAccount);
		return customers.save(new Customer(modifiedAccount));

    }

	public void save(Customer customer){
		customers.save(customer);
		userAccounts.save(customer.getUserAccount());
	}

    /**
     * Returns all {@link Customer}s currently available in the system.
     *
     * @return all {@link Customer} entities.
     */
    public Streamable<Customer> findAll() {
        return customers.findAll();
    }

    /**
     * Returns the {@link Customer}s with the given UUID.
     *
     * @return a {@link Customer} entity.
     */
    public Optional<Customer> findByUUID(UUID id) {
        return customers.findById(id);
    }
}

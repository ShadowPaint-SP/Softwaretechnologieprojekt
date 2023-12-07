package campingplatz.customer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;

class CustomerManagementUnitTests {

	@Test
	void createsUserAccountWhenCreatingACustomer() {

		CustomerRepository repository = mock(CustomerRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any(Role.class))).thenReturn(userAccount);

		CustomerManagement customerManagement = new CustomerManagement(repository, userAccountManager);

		RegistrationForm form = new RegistrationForm("name", "last", "password");
		Customer customer = customerManagement.create(form.getName(), UnencryptedPassword.of(form.getPassword()),
				Customer.Roles.CUSTOMER.getValue());

		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(Customer.Roles.CUSTOMER.getValue()));

		assertThat(customer.getUserAccount()).isNotNull();
	}
}

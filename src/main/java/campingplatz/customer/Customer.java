package campingplatz.customer;

import jakarta.persistence.*;
import lombok.Getter;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import java.util.UUID;

// Salespoint bietet nur eine UserAccount Verwaltung, für weitere Attribute sollte eine extra
// Klasse geschrieben werden. Unser Kunde hat noch eine Adresse, das bietet der UserAccount nicht.
// Um den Customer in die Datenbank zu bekommen, schreiben wir ein CustomerRepository.

@Entity
public class Customer {
	@Getter
	private @Id UUID id = UUID.randomUUID();

	/*
	 * Every Customer has a User Account.
	 */
	@Getter
	@OneToOne
	private UserAccount userAccount;

	/**
	 * Default constructor.
	 */
	public Customer() {
	}

	/**
	 * Constructs a new customer with the given user account.
	 *
	 * @param userAccount the user account to associate with the customer
	 */
	public Customer(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * Returns the username of the customer.
	 *
	 * @return the username of the customer
	 */
	public String getUsername() {
		return userAccount.getUsername();
	}

	/**
	 * Returns the first name of the customer.
	 *
	 * @return the first name of the customer
	 */
	public String getFirstName() {
		return userAccount.getFirstname();
	}

	/**
	 * Returns the last name of the customer.
	 *
	 * @return the last name of the customer
	 */
	public String getLastName() {
		return userAccount.getLastname();
	}

	/**
	 * Returns the role of the customer.
	 *
	 * @return the role of the customer
	 */
	public Role getRole() {
		var roles = userAccount.getRoles().toList();
		return roles.get(0);
	}

	/**
	 * Sets the role of the customer.
	 *
	 * @param r the role to set for the customer
	 */
	public void setRole(Role r) {
		// delete all existing roles
		var roles = userAccount.getRoles().toList();
		for (var role : roles) {
			userAccount.remove(role);
		}

		// add the wanted role
		userAccount.add(r);
	}

	/**
	 * Enum representing the possible roles a customer can have in the system.
	 */
	public static enum Roles {

		/**
		 * No specific role assigned.
		 */
		NONE(Role.of("")),

		/**
		 * Role for regular customers.
		 */
		CUSTOMER(Role.of("CUSTOMER")),

		/**
		 * Role for employees.
		 */
		EMPLOYEE(Role.of("EMPLOYEE")),

		/**
		 * Role for the manager or owner.
		 */
		BOSS(Role.of("BOSS"));

		private Role role;

		/**
		 * Constructor for the enum.
		 *
		 * @param role the role to assign to the enum value
		 */
		Roles(Role role) {
			this.role = role;
		}

		/**
		 * Returns the role associated with the enum value.
		 *
		 * @return the role associated with the enum value
		 */
		public Role getValue() {
			return role;
		}

		/**
		 * Returns the enum value corresponding to the given number.
		 *
		 * @param i the number to map to an enum value
		 * @return the enum value corresponding to the given number
		 */
		public static Role fromNumber(Integer i) {
			return switch (i) {
				case 0 -> NONE.getValue();
				case 1 -> CUSTOMER.getValue();
				case 2 -> EMPLOYEE.getValue();
				case 3 -> BOSS.getValue();
				default -> NONE.getValue();
			};

		}
	}

}

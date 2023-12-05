package campingplatz.customer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Salespoint bietet nur eine UserAccount Verwaltung, für weitere Attribute sollte eine extra
// Klasse geschrieben werden. Unser Kunde hat noch eine Adresse, das bietet der UserAccount nicht.
// Um den Customer in die Datenbank zu bekommen, schreiben wir ein CustomerRepository.

@Entity
public class Customer {

    private @Id UUID id = UUID.randomUUID();

    @Getter @Setter
	private String address;

    // Jedem Customer ist genau ein UserAccount zugeordnet, um später über den
    // UserAccount an den
    // Customer zu kommen, speichern wir den hier
    @OneToOne //
    private UserAccount userAccount;

    public Customer() {
    }

    public Customer(UserAccount userAccount) {
        this.userAccount = userAccount;

    }

    public UUID getId() {
        return id;
    }

	public String getUsername(){
		return userAccount.getUsername();
	}

    public UserAccount getUserAccount() {
        return userAccount;
    }

	public Role getRole(){
		var roles = userAccount.getRoles().toList();
		return roles.get(0);
	}
	public void setRole(Role r){
		// delete all existing roles
		var roles = userAccount.getRoles().toList();
		for (var role : roles){
			userAccount.remove(role);
		}

		// add the wanted role
		userAccount.add(r);
	}




	public static enum Roles {
		NONE(Role.of("")),
		CUSTOMER(Role.of("CUSTOMER")),
		EMPLOYEE( Role.of("EMPLOYEE")),
		BOSS(Role.of("BOSS"));

		private Role role;

		Roles(Role role){
			this.role = role;
		}

		public Role getValue(){
			return role;
		}


		public static Role fromNumber(Integer i){
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

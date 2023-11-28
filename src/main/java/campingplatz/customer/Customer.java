package campingplatz.customer;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.UserAccount;
import campingplatz.customer.Customer.CustomerIdentifier;

import java.io.Serializable;
import java.util.UUID;

// Salespoint bietet nur eine UserAccount Verwaltung, für weitere Attribute sollte eine extra
// Klasse geschrieben werden. Unser Kunde hat noch eine Adresse, das bietet der UserAccount nicht.
// Um den Customer in die Datenbank zu bekommen, schreiben wir ein CustomerRepository.

@Entity
public class Customer extends AbstractAggregateRoot<CustomerIdentifier> {

    private @EmbeddedId CustomerIdentifier id = new CustomerIdentifier();

    private String address;

    // Jedem Customer ist genau ein UserAccount zugeordnet, um später über den
    // UserAccount an den
    // Customer zu kommen, speichern wir den hier
    @OneToOne //
    private UserAccount userAccount;

	@SuppressWarnings({ "unused", "deprecation" })
    public Customer() {
    }

    public Customer(UserAccount userAccount, String address) {
        this.userAccount = userAccount;
        this.address = address;
    }

    @Override
    public CustomerIdentifier getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    @Embeddable
    public static final class CustomerIdentifier implements Identifier, Serializable {

        private static final long serialVersionUID = 7740660930809051850L;
        private final @SuppressWarnings("unused") UUID identifier;

        /**
         * Creates a new unique identifier for {@link Customer}s.
         */
        CustomerIdentifier() {
            this(UUID.randomUUID());
        }

        /**
         * Only needed for property editor, shouldn't be used otherwise.
         *
         * @param identifier The string representation of the identifier.
         */
        CustomerIdentifier(UUID identifier) {
            this.identifier = identifier;
        }


		/*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            final int prime = 31;
            int result = 1;

            result = prime * result + (identifier == null ? 0 : identifier.hashCode());

            return result;
        }


        @Override
        public boolean equals(Object obj) {

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof CustomerIdentifier that)) {
                return false;
            }

            return this.identifier.equals(that.identifier);
        }
    }
}

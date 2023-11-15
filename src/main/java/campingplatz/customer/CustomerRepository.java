package campingplatz.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import campingplatz.customer.Customer.CustomerIdentifier;


interface CustomerRepository extends CrudRepository<Customer, CustomerIdentifier> {

    /**
     * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable}
     * instead of {@link Iterable}.
     */
    @Override
    Streamable<Customer> findAll();
}

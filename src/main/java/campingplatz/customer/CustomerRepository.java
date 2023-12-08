package campingplatz.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.UUID;

interface CustomerRepository extends CrudRepository<Customer, UUID> {

    /**
     * Re-declared {@link CrudRepository#findAll()} to return a {@link Streamable}
     * instead of {@link Iterable}.
     */
    @Override
    Streamable<Customer> findAll();
}

package campingplatz.equip;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * An extension of {@link Catalog} to add specific query methods.
 *
 */
@Repository
public interface SportItemCatalog extends Catalog<SportItem> {
	Optional<SportItem> findById(Product.ProductIdentifier id);
}

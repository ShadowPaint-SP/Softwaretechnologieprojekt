package campingplatz.equip;


import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportItemCatalog extends Catalog<SportItem> {

	Optional<SportItem> findById(Product.ProductIdentifier id);
}

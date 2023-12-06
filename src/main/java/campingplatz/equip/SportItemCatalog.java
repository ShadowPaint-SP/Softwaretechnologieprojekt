package campingplatz.equip;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;

import java.util.Optional;

public interface SportItemCatalog extends Catalog<SportItem> {

	Optional<SportItem> findById(Product.ProductIdentifier id);
}

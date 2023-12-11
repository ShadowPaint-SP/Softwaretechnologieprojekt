package campingplatz.equip;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.Product;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

public interface SportItemCatalog extends Catalog<SportItem> {
	Optional<SportItem> findById(Product.ProductIdentifier id);

	// interface representing the state of the plot catalog site
	// most notably the data of the plot filter query


}

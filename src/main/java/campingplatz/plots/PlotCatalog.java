package campingplatz.plots;

import org.salespointframework.catalog.Catalog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * An extension of {@link Catalog} to add specific query methods.
 *
 */
@Repository
public interface PlotCatalog extends Catalog<Plot> {

    List<Plot> findByState(Plot.State state);

}

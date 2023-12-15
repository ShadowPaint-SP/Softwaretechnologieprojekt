package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.salespointframework.core.Currencies.EURO;

/**
 * An extension of {@link Catalog} to add specific query methods.
 *
 */
@Repository
public interface PlotCatalog extends Catalog<Plot> {

	List<Plot> findByState(Plot.State state);


}

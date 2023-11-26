package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

/**
 * An extension of {@link Catalog} to add specific query methods.
 *
 */
public interface PlotCatalog extends Catalog<Plot> {
	static final Sort DEFAULT_SORT = Sort.sort(Plot.class).by(Plot::getName).descending();
	Streamable<Plot> findByType(Plot.ParkingLot lotType, Sort sort);

	default Streamable<Plot> findByType(Plot.ParkingLot type) {
		return findByType(type, DEFAULT_SORT);
	}
    default List<Plot> filter(SiteState query) {
        // we just use filter, instead of specialized database queries.
        // the number of plots in the catalog is not big enough for it to matter
        return findAll().filter(plot -> {
            boolean isMatch = true;

            var priceMin = query.getPriceMin();
            if (priceMin != null && plot.getPrice().isLessThan(Money.of(priceMin, EURO))) {
                isMatch = false;
            }

            var priceMax = query.getPriceMax();
            if (isMatch && priceMax != null && plot.getPrice().isGreaterThan(Money.of(priceMax, EURO))) {
                isMatch = false;
            }

            var sizeMin = query.getSizeMin();
            if (isMatch && sizeMin != null && plot.getSize() < sizeMin) {
                isMatch = false;
            }

            var sizeMax = query.getSizeMax();
            if (isMatch && sizeMax != null && plot.getSize() > sizeMax) {
                isMatch = false;
            }

            var parkingMin = query.getParking();
            if (isMatch && parkingMin != null && plot.getParking().size < parkingMin) {
                isMatch = false;
            }

            return isMatch;

        }).toList();

    }

    // interface representing the state of the plot catalog site
    // most notably the data of the plot filter query
    interface SiteState {

        ////////////////////
        // plot filter query

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getArrival();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getDeparture();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedArrival() {
            if (getArrival() == null) {
                return LocalDate.now();
            }
            return getArrival();
        }

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedDeparture() {
            if (getDeparture() == null) {
                return LocalDate.now().plusDays(1);
            }
            return getDeparture();
        }

        // used in plotcatalog.html for min arrival date.
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate minArrivalDate() {
            return LocalDate.now();
        }

        Double getSizeMin();

        Double getSizeMax();

        Double getPriceMin();

        Double getPriceMax();

        Integer getParking();

        ////////////////////
        // other site data

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getFirstWeekDate();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        void setFirstWeekDate(LocalDate value);

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedFirstWeekDate() {
            if (getFirstWeekDate() == null) {
                var weekDay = getDefaultedArrival().getDayOfWeek().getValue() - 1;
                var weekBegin = getDefaultedArrival().minusDays(weekDay);
                return weekBegin;
            }
            return getFirstWeekDate();
        }

    }

}

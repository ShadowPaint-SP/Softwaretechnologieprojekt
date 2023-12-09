package campingplatz.seasonalplots;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Catalog;

import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

public interface SeasonalPlotCatalog extends Catalog<SeasonalPlot> {

	default List<SeasonalPlot> seasonalFilter(SeasonalSiteState query) {
		return findAll().filter(seasonalPlot -> {
			boolean isMatch = true;

			var priceMin = query.getPriceMin();
			if (priceMin != null && seasonalPlot.getPrice().isLessThan(Money.of(priceMin, EURO))) {
				isMatch = false;
			}

			var priceMax = query.getPriceMax();
			if (isMatch && priceMax != null && seasonalPlot.getPrice().isGreaterThan(Money.of(priceMax, EURO))) {
				isMatch = false;
			}

			var sizeMin = query.getSizeMin();
			if (isMatch && sizeMin != null && seasonalPlot.getSize() < sizeMin) {
				isMatch = false;
			}

			var sizeMax = query.getSizeMax();
			if (isMatch && sizeMax != null && seasonalPlot.getSize() > sizeMax) {
				isMatch = false;
			}

			var parkingMin = query.getParking();
			if (isMatch && parkingMin != null && seasonalPlot.getParking().size < parkingMin) {
				isMatch = false;
			}

			return isMatch;

		}).toList();
	}


	interface SeasonalSiteState {
		Double getSizeMin();

		Double getSizeMax();

		Double getPriceMax();

		Double getPriceMin();

		Integer getParking();
	}
}

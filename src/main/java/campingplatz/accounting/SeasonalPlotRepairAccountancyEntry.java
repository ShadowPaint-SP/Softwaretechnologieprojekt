package campingplatz.accounting;

import campingplatz.plots.Plot;
import campingplatz.seasonalplots.SeasonalPlot;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SeasonalPlotRepairAccountancyEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(SeasonalPlot plot) {

		List<String> ret = new ArrayList<>();

		ret.add("Reparatur " + plot.getName().replace("\n", " "));
		ret.add(""); ret.add(""); ret.add("");

		return ret;
	}

	public SeasonalPlotRepairAccountancyEntry(Double cost, SeasonalPlot plot) {
		super(Money.of(-cost, Currencies.EURO), descriptions(plot));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public SeasonalPlotRepairAccountancyEntry() {

	}

}

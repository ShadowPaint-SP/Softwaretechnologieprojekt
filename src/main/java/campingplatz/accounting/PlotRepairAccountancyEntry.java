package campingplatz.accounting;

import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.utils.Utils;
import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Entity
public class PlotRepairAccountancyEntry extends ExtendedAccountancyEntry {

	private static List<String> descriptions(Plot plot) {

		List<String> ret = new ArrayList<>();

		ret.add("Reparatur " + plot.getName().replace("\n", " "));
		ret.add(""); ret.add(""); ret.add("");

		return ret;
	}

	public PlotRepairAccountancyEntry(Double cost, Plot plot) {
		super(Money.of(-cost, Currencies.EURO), descriptions(plot));
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public PlotRepairAccountancyEntry() {

	}

}

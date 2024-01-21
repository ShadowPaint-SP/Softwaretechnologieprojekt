package campingplatz.accounting;

import campingplatz.plots.Plot;
import campingplatz.seasonalplots.SeasonalPlot;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.salespointframework.core.Currencies.EURO;

public class AccountingEntryTest {

    @Test
    public void SeasonalPlotRepairAccountancyEntryConstructorTest() {
        SeasonalPlot sp = new SeasonalPlot("lol",
                100.0,Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                100.00,
                100.00,
                "",""
                );
        assertDoesNotThrow(() -> {
            new SeasonalPlotRepairAccountancyEntry(10.0, sp);
        }, "SeasonalPlotRepairAccountancyEntry constructor failed");
    }

    @Test
    public void PlotRepairAccountancyEntryConstructorTest() {
        Plot sp = new Plot("lol",100.0,
                Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                "","");
        assertDoesNotThrow(() -> {
            new PlotRepairAccountancyEntry(10.0, sp);
        }, "SeasonalPlotRepairAccountancyEntry constructor failed");
    }

}

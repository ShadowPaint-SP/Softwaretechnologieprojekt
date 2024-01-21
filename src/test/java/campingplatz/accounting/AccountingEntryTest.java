package campingplatz.accounting;

import campingplatz.plots.Plot;
import campingplatz.seasonalplots.SeasonalPlot;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.salespointframework.core.Currencies.EURO;

public class AccountingEntryTest {

    private Plot dp;
    private SeasonalPlot dsp;

    @BeforeEach
    void setUp() {
        this.dp = new Plot("lol",100.0,
                Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                "","");
        this.dsp = new SeasonalPlot("lol",
                100.0,Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                100.00,
                100.00,
                "",""
        );
    }
    @Test
    public void SeasonalPlotRepairAccountancyEntryConstructorTest() {
        assertDoesNotThrow(() -> {
            new SeasonalPlotRepairAccountancyEntry(10.0, this.dsp);
        }, "SeasonalPlotRepairAccountancyEntry constructor failed");
    }

    @Test
    public void PlotRepairAccountancyEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new PlotRepairAccountancyEntry(10.0, this.dp);
        }, "SeasonalPlotRepairAccountancyEntry constructor failed");
    }

}

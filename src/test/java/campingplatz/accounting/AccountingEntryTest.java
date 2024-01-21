package campingplatz.accounting;

import campingplatz.equip.SportItem;
import campingplatz.plots.Plot;
import campingplatz.seasonalplots.SeasonalPlot;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
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

}

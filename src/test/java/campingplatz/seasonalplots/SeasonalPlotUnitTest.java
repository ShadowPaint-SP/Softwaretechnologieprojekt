package campingplatz.seasonalplots;

import campingplatz.plots.Plot;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class SeasonalPlotUnitTest {

    SeasonalPlot plot;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        plot = new SeasonalPlot("PLatz am See", 10.0, Money.of(10, EURO), Plot.ParkingLot.CAMPER_PARKING,
                10.0, 10.0, "", "Wunderschön");
    }

    @Test
    void init_SeasonalPLot(){
        SeasonalPlot plot = new SeasonalPlot("PLatz am See", 10.0, Money.of(10, EURO), Plot.ParkingLot.CAMPER_PARKING,
                10.0, 10.0, "", "Wunderschön");
    }

    @Test
    void settlementElectricityTest() {
        assertEquals(0.0, plot.settlementElectricity(5.0, 2.0),
                "seasonalplot.settlementElectricity, return not 0.0, when new Water Meter is to low");
        assertEquals(10.0, plot.settlementElectricity(20.0, 0.0),
                "seasonalplot.settlementElectricity, return wrong difference");
        assertEquals(-5.0, plot.settlementElectricity(15, 10.0),
                "seasonalplot.settlementElectricity, set the Difference not back when the first was to high");
    }

    @Test
    void settlementWaterTest() {
        assertEquals(0.0, plot.settlementWater(5.0, 2.0),
                "seasonalplot.settlementWater, return not 0.0, when new water Meter is to low");
        assertEquals(10.0, plot.settlementElectricity(20.0, 0.0),
                "seasonalplot.settlementWater, return wrong difference");
        assertEquals(-5.0, plot.settlementElectricity(15, 10.0),
                "seasonalplot.settlementwater, set the Difference not back when the first was to high");
    }

    @Test
    void getArrivalTest() {
        var first = SeasonalPlot.getArrival(LocalDateTime.of(2023, 3, 1, 0, 0));
        assertEquals(LocalDate.of(2023, 4,1),
                LocalDate.of(first.getYear(), first.getMonth(), first.getDayOfMonth()),
            "arrival before 1.4. is not 1.4.");
        var second = SeasonalPlot.getArrival(LocalDateTime.of(2023, 6, 1, 0, 0));
        assertEquals(LocalDate.of(2023, 7,1),
                LocalDate.of(second.getYear(), second.getMonth(), second.getDayOfMonth()),
            "arrival after 1.4. is not first Day of next month");
    }
     @Test
    void getDepartureTest() {
        var first = plot.getDeparture(LocalDateTime.now());
        assertEquals(LocalDate.now().withMonth(10).withDayOfMonth(31),
                LocalDate.of(first.getYear(), first.getMonth(), first.getDayOfMonth()), "departure is wrong");
     }

}

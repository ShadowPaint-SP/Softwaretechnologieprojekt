package campingplatz.seasonalplots;

import campingplatz.plots.Plot;

import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

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

}

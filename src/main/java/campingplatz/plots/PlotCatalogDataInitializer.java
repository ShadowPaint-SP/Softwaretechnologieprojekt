package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;


@Component
@Order(20)
class PlotCatalogDataInitializer implements DataInitializer {
    private final PlotCatalog plotCatalog;

    PlotCatalogDataInitializer(PlotCatalog plotCatalog) {
        this.plotCatalog = plotCatalog;
    }

    @Override
    public void initialize() {
        // TODO Customer und nicht nur Name übergeben
        plotCatalog.save(new Plot("1. Platz", 15.0, Money.of(20, EURO), Plot.ParkingLot.NONE));
        plotCatalog
                .save(new Plot("2. Platz", 30.0, Money.of(15, EURO), Plot.ParkingLot.BIKE_PARKING));
        plotCatalog.save(new Plot("3. Platz", 20.0, Money.of(35, EURO), Plot.ParkingLot.NONE));
        plotCatalog.save(new Plot("4. Platz", 7.0, Money.of(15, EURO), Plot.ParkingLot.NONE));
        plotCatalog.save(
                new Plot("5. Platz", 150.0, Money.of(15, EURO), Plot.ParkingLot.CAMPER_PARKING));
        plotCatalog
                .save(new Plot("6. Platz", 80.0, Money.of(40, EURO), Plot.ParkingLot.CAR_PARKING));
        plotCatalog.save(
                new Plot("7. Platz", 100.0, Money.of(40, EURO), Plot.ParkingLot.CAMPER_PARKING));
        plotCatalog.save(new SeasonalPlots("1. Saison Plot", 50.0, Money.of(100, EURO), Plot.ParkingLot.NONE,
                0, 0, "hans", SeasonalPlots.PaymentMethod.SEASONAL));
        plotCatalog.save(new SeasonalPlots("2. Saison Plot", 30.0, Money.of(80, EURO), Plot.ParkingLot.NONE,
                200, 533, "hans", SeasonalPlots.PaymentMethod.SEASONAL));
        plotCatalog.save(new SeasonalPlots("2. Saison Plot", 120.0, Money.of(150, EURO), Plot.ParkingLot.NONE,
                232932, 334883, "hans", SeasonalPlots.PaymentMethod.SEASONAL));
    }
}

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
        plotCatalog.save(new Plot("Platz im Wald I", 15.0, Money.of(20, EURO), Plot.ParkingLot.NONE,
                "/img/camp_1_1/camp_1.png",
                "Ein ausgezeichneter Platz zum Zelten im Wald."));
        plotCatalog.save(new Plot("Platz im Wald II", 30.0, Money.of(15, EURO), Plot.ParkingLot.BIKE_PARKING,
                "/img/camp_1_1/camp_2.png",
                "Ein toller Platz authentischen Vogelattrappen."));
        plotCatalog.save(new Plot("Platz am See I", 20.0, Money.of(35, EURO), Plot.ParkingLot.NONE,
                "/img/camp_1_1/camp_3.png",
                "Fast keine Mücken. (Stand Januar 1991)"));
        plotCatalog.save(new Plot("Platz am See II", 7.0, Money.of(15, EURO), Plot.ParkingLot.NONE,
                "/img/camp_1_1/camp_4.png",
                "Diese Sträucher sind echt!"));
        plotCatalog.save(new Plot("Platz auf dem See", 150.0, Money.of(15, EURO), Plot.ParkingLot.CAMPER_PARKING,
                "/img/camp_1_1/camp_5.png",
                "Mit Parkplatz für Camper."));
        plotCatalog.save(new Plot("Platz am See IV", 80.0, Money.of(40, EURO), Plot.ParkingLot.CAR_PARKING,
                "/img/camp_1_1/camp_6.png",
                "Mit malerischer Aussicht!"));
        plotCatalog.save(new Plot("Platz auf dem See", 100.0, Money.of(40, EURO), Plot.ParkingLot.CAMPER_PARKING,
                "/img/camp_1_1/camp_7.png",
                "Se(e)hen und staunen ;)"));

    }
}

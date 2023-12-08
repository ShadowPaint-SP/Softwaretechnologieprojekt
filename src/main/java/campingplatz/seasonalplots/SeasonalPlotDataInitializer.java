package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalog;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
class SeasonalPlotCatalogDataInitializer implements DataInitializer{
	private final SeasonalPlotCatalog seasonalPlotCatalog;

	SeasonalPlotCatalogDataInitializer(SeasonalPlotCatalog seasonalPlotCatalog) {
		this.seasonalPlotCatalog = seasonalPlotCatalog;
	}

	@Override
	public void initialize() {
		seasonalPlotCatalog.save(new SeasonalPlot("Platz am kleinem See", 60.0, Money.of(100, EURO), Plot.ParkingLot.CAMPER_PARKING, 0.0, 34.0, "/imp/camp_1_1/scamp_1.png", "Grandiose Aussicht, Sonnengarantie"));
		seasonalPlotCatalog.save(new SeasonalPlot("Platz unter Lärchen", 40.0, Money.of(80, EURO), Plot.ParkingLot.CAMPER_PARKING, 0.0, 0.0, "/imp/camp_1_1/scamp_2.png", "Im Herbst leider etwas nadelig, ansonsten Top."));
		seasonalPlotCatalog.save(new SeasonalPlot("Platz am großen See I", 60.0, Money.of(100, EURO), Plot.ParkingLot.CAR_PARKING, 30.0, 37.0, "/imp/camp_1_1/scamp_3.png", "Wunderschöner See mit viel zu viel Berg."));
		seasonalPlotCatalog.save(new SeasonalPlot("Zeltplatz im Wald I", 20.0, Money.of(30, EURO), Plot.ParkingLot.BIKE_PARKING, 0.0, 0.0, "/imp/camp_1_1/scamp_4.png", "Gut im Wald gelegen"));
		seasonalPlotCatalog.save(new SeasonalPlot("Zeltplatz im Wald II", 80.0, Money.of(120, EURO), Plot.ParkingLot.CAR_PARKING, 2345.0, 27.0, "/imp/camp_1_1/scamp_5.png", "Wunderschönes fleckchen Erde."));
		seasonalPlotCatalog.save(new SeasonalPlot("Platz an der Sonne", 90.0, Money.of(110, EURO), Plot.ParkingLot.CAMPER_PARKING, 50.0, 0.0, "/imp/camp_1_1/scamp_6.png", "Perfekte Sonnenuntergänge"));
	}

}

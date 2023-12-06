package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class SportItemCatalogDataInitializer implements DataInitializer {
	private SportItemCatalog sportItemCatalog;

	SportItemCatalogDataInitializer(SportItemCatalog sportItemCatalog) {
		this.sportItemCatalog = sportItemCatalog;
	}

	@Override
	public void initialize() {
		sportItemCatalog.save(new SportItem("Fußball", Money.of(0.5, EURO),
			Money.of(16, EURO), "Ball", 10));
		sportItemCatalog.save(new SportItem("Basketball", Money.of(0.5, EURO),
			Money.of(20, EURO), "Ball", 8));
		sportItemCatalog.save(new SportItem("Federballset", Money.of(1, EURO),
			Money.of(30, EURO), "Federball", 3));
		sportItemCatalog.save(new SportItem("Tischtennisgarnituren", Money.of(2, EURO),
			Money.of(100, EURO), "Tischtennis", 2));
		sportItemCatalog.save(new SportItem("Volleyball", Money.of(0.5, EURO),
			Money.of(14, EURO), "Volleyball", 6));
		sportItemCatalog.save(new SportItem("Volleyballnetz", Money.of(2, EURO),
			Money.of(25, EURO), "Volleyball", 3));
	}
}

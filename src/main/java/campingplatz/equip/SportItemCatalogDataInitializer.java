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
			Money.of(16, EURO), "Ball", 10,
			"/img/equip/football.png",
			"Fußball macht Spaß"));
		sportItemCatalog.save(new SportItem("Basketball", Money.of(0.5, EURO),
			Money.of(20, EURO), "Ball", 8,
			"/img/equip/basketball.png",
			"Basketball macht Spaß"));
		sportItemCatalog.save(new SportItem("Federballset", Money.of(1, EURO),
			Money.of(30, EURO), "Federball", 3,
			"/img/equip/badminton.png",
			"Federballset macht Spaß"));
		sportItemCatalog.save(new SportItem("Tischtennisgarnituren", Money.of(2, EURO),
			Money.of(100, EURO), "Tischtennis", 2,
			"/img/equip/table_tennis.png",
			"Tischtennis macht Spaß"));
		sportItemCatalog.save(new SportItem("Volleyball", Money.of(0.5, EURO),
			Money.of(14, EURO), "Volleyball", 6,
			"/img/equip/volleyball.png",
			"Volleyball macht Spaß"));
		sportItemCatalog.save(new SportItem("Volleyballnetz", Money.of(2, EURO),
			Money.of(25, EURO), "Volleyball", 3,
			"/img/equip/volleyball_net.png",
			"Volleyball macht Spaß"));
	}
}

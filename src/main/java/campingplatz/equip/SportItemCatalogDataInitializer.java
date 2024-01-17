package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;


/**
 * Data initializer for populating the {@link SportItemCatalog} with predefined sport items.
 * <p>
 * This class implements the {@link DataInitializer} interface to provide a mechanism for initializing
 * the sport item catalog with predefined items when the application starts.
 * </p>
 */
@Component
public class SportItemCatalogDataInitializer implements DataInitializer {
	private SportItemCatalog sportItemCatalog;

    /**
     * Constructs a {@code SportItemCatalogDataInitializer} with the specified {@link SportItemCatalog}.
     *
     * @param sportItemCatalog The catalog to be initialized with sport items.
     */
	SportItemCatalogDataInitializer(SportItemCatalog sportItemCatalog) {
		this.sportItemCatalog = sportItemCatalog;
	}

    /**
     * Initializes the sport item catalog with predefined sport items.
     * <p>
     * This method is called during the application startup to add predefined sport items to the catalog.
     * </p>
     */
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
			"Federball macht Spaß"));
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

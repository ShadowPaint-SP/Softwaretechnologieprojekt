package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

@Component
public class SportItemCatalogDataInitializer implements DataInitializer {
	private SportItemCatalog sportItemCatalog;

	SportItemCatalogDataInitializer(SportItemCatalog sportItemCatalog){
		this.sportItemCatalog = sportItemCatalog;
	}
	@Override
	public void initialize() {
		sportItemCatalog.save(new SportItem("Ball", Money.of(10,EURO),"Ball"));
	}
}

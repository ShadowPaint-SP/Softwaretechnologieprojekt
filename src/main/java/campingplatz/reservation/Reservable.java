package campingplatz.reservation;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import java.time.temporal.ChronoUnit;

public class Reservable extends Product {

	@SuppressWarnings({ "unused", "deprecation" })
	public Reservable() {
	}
	public Reservable(String name, Money price) {
		super(name, price);
	}

	// supposed to overridden
	public static ChronoUnit getIntervalUnit(){
		return null;
	}

}

package campingplatz.equip;

import jakarta.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class SportItem extends Product {

	@SuppressWarnings({ "unused", "deprecation" })
	public SportItem() {
	}

	public SportItem(String name, Money price, String category) {
		super(name, price);
		this.addCategory(category);
	}

}

package campingplatz.equip;

import campingplatz.customer.Customer;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
@Entity
@EqualsAndHashCode
public class SportItem extends Product {
	@Getter
	private String name;

	@SuppressWarnings({ "unused", "deprecation" })
	public SportItem(){}

	public SportItem(String name, Money price, String category){
		super(name, price);
		this.addCategory(category);
	}

}

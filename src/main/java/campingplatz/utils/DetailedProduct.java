package campingplatz.utils;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Setter
@Getter
@Entity
public class DetailedProduct extends Product {
	private String imagePath;
	private String desc;

	@SuppressWarnings({ "deprecation" })
	public DetailedProduct() {
	}

	public DetailedProduct(String name, Money price) {
		super(name, price);
		this.imagePath = "/img/placeholder.png";
		this.desc = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam";
	}

	public DetailedProduct(String name, Money price, String imagePath, String description) {
		super(name, price);
		this.imagePath = imagePath;
		this.desc = description;
	}


}

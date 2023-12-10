package campingplatz.utils;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;


@Entity
public class DetailedProduct extends Product {

	@Setter
	@Getter
	private String imagePath;

	@Setter
	@Getter
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

package campingplatz.utils;

import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Getter
public class DetailedProduct extends Product {
	@Setter
	private String imagePath;
	@Setter
	private String desc;

	@SuppressWarnings({ "deprecation" })
	public DetailedProduct() {
	}

	public DetailedProduct(String name, Money price) {
		super(name, price);
		this.imagePath = "/img/placeholder.png";
		this.desc = "";
	}

	public DetailedProduct(String name, Money price, String image_path, String description) {
		super(name, price);
		this.imagePath = image_path;
		this.desc = description;
	}

}

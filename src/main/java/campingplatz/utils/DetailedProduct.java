package campingplatz.utils;

import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

public class DetailedProduct extends Product {
	@Getter
	@Setter
	private String image_path;
	@Getter
	@Setter
	private String desc;

	@SuppressWarnings({ "unused", "deprecation" })
	public DetailedProduct(){}
	public DetailedProduct(String name, Money price){
		super(name, price);
		// TODO placeholder path einfügen
		this.image_path = "";
		this.desc = "";
	}

	public DetailedProduct(String name, Money price, String image_path, String description){
		super(name, price);
		this.image_path = image_path;
		this.desc = description;
	}
}

package campingplatz.utils;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

/**
 * Represents a detailed product in the campingplatz management system.
 * This class extends the base Product class and adds additional fields for
 * imagePath and description.
 */
@Entity
public class DetailedProduct extends Product {

	/**
	 * The path to the image associated with this product.
	 */
	@Setter
	@Getter
	private String imagePath;

	/**
	 * The description of this product.
	 */
	@Setter
	@Getter
	private String desc;

	/**
	 * Default constructor for DetailedProduct.
	 */
	@SuppressWarnings({ "deprecation" })
	public DetailedProduct() {
	}

	/**
	 * Constructor for DetailedProduct with name and price.
	 * Sets the imagePath to a default placeholder image and description to a
	 * default Lorem Ipsum text.
	 *
	 * @param name  The name of the product.
	 * @param price The price of the product.
	 */
	public DetailedProduct(String name, Money price) {
		super(name, price);
		this.imagePath = "/img/placeholder.png";
		this.desc = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam";
	}

	/**
	 * Constructor for DetailedProduct with name, price, imagePath, and description.
	 *
	 * @param name        The name of the product.
	 * @param price       The price of the product.
	 * @param imagePath   The path to the image associated with this product.
	 * @param description The description of this product.
	 */
	public DetailedProduct(String name, Money price, String imagePath, String description) {
		super(name, price);
		this.imagePath = imagePath;
		this.desc = description;
	}
}

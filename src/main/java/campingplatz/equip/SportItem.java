package campingplatz.equip;

import campingplatz.utils.DetailedProduct;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Getter
@Entity
public class SportItem extends DetailedProduct {


	@Setter
	private int amount;

	@Setter
	private Money deposit;


	@SuppressWarnings({ "unused" })
	public SportItem() {
	}

	// rentalFee, price = Leihgebühr | deposit = Kaution


	public SportItem(String name, Money price, Money deposit, String category) {
		super(name, price);
		this.addCategory(category);
		this.amount = 0;
		this.deposit = deposit;
	}
	public SportItem(String name, Money rentalFee, Money deposit, String category, int amount) {
		super(name, rentalFee);
		this.addCategory(category);
		this.amount = amount;
		this.deposit = deposit;
	}

	public SportItem(String name, Money rentalFee, Money deposit, String category, int amount, String imagePath, String description) {
		super(name, rentalFee, imagePath, description);
		this.addCategory(category);
		this.amount = amount;
		this.deposit = deposit;
	}

}
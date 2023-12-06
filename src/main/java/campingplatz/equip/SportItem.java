package campingplatz.equip;

import campingplatz.utils.DetailedProduct;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Setter
@Getter
@Entity
public class SportItem extends DetailedProduct {


	private int amount;

	private Money deposit;

	public SportItem() {
	}

	// rentalFee, price = Leihgebühr | deposit = Kaution
	public SportItem(String name, Money rentalFee, Money deposit, String category, int amount, String imagePath,
			String description) {
		super(name, rentalFee, imagePath, description);
		this.addCategory(category);
		this.amount = amount;
		this.deposit = deposit;
	}

}

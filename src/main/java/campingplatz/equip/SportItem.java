package campingplatz.equip;

import campingplatz.utils.DetailedProduct;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.time.LocalTime;

@Getter
@Entity
public class SportItem extends DetailedProduct {

	@Getter
	@Setter
	private int amount;

	@Setter
	private Money deposit;

	@Getter
	@Setter
	private LocalTime startTime;

	@Getter
	@Setter
	private LocalTime endTime;


	@SuppressWarnings({ "unused", "deprecation" })
	public SportItem() {
	}

	// rental_fee, price = Leihgebühr | deposit = Kaution
	public SportItem(String name, Money price, Money deposit, String category) {
		super(name, price);
		this.addCategory(category);
		this.amount = 0;
		this.deposit = deposit;
	}

	public SportItem(String name, Money rental_fee, Money deposit, String category, int amount) {
		super(name, rental_fee);
		this.addCategory(category);
		this.amount = amount;
		this.deposit = deposit;
	}

	public SportItem(String name,
			Money rental_fee,
			Money deposit,
			String category,
			int amount,
			String image_path,
			String description) {
		super(name, rental_fee, image_path, description);
		this.addCategory(category);
		this.amount = amount;
		this.deposit = deposit;
	}

}

package campingplatz.plots;

import campingplatz.utils.Priced;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.UUID;



@Entity
public class Issue implements Priced {

	private @Id UUID id;

	@Getter
	@Setter
	private Money cost;

	@Getter
	@Setter
	private String description;


	public Issue() {
		this.id = UUID.randomUUID();
	}

	public Issue(Money cost, String description) {
		this.id = UUID.randomUUID();
		this.cost = cost;
		this.description = description;
	}


	@Override
	public MonetaryAmount getPrice() {
		return this.cost;
	}

	@Override
	public MonetaryAmount getPreDiscountPrice() {
		return this.cost;
	}
}

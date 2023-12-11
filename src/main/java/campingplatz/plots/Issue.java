package campingplatz.plots;

import campingplatz.utils.Priced;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.UUID;


@Getter
@Entity
public class Issue implements Priced {

	private @Id UUID id;
	@Setter
	private Money cost;
	@Setter
	private String description;

	public Issue(Money cost, String description) {
		this.cost = cost;
		this.description = description;
	}

	@Override
	public MonetaryAmount getPrice() {
		return this.cost;
	}
}

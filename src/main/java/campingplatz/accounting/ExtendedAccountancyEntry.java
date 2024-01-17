package campingplatz.accounting;

import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.List;
import javax.money.MonetaryAmount;
import org.salespointframework.accountancy.AccountancyEntry;

@Entity
public class ExtendedAccountancyEntry extends AccountancyEntry {

	@Getter
	private List<String> descriptions;

	public ExtendedAccountancyEntry(MonetaryAmount value, List<String> descriptions) {
		super(value, "");
		this.descriptions = descriptions;
	}

	// need default constructor
	@SuppressWarnings("deprecation")
	public ExtendedAccountancyEntry() {

	}
}
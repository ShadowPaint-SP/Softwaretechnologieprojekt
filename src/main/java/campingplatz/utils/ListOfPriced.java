package campingplatz.utils;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.ArrayList;

import static org.salespointframework.core.Currencies.EURO;

public class ListOfPriced<T extends Priced> extends ArrayList<T> implements Priced {

	public MonetaryAmount getPrice() {
		return this.stream()
				.map(Priced::getPrice)
				.reduce(MonetaryAmount::add)
				.orElse(Money.of(0, EURO));
	}

	public MonetaryAmount getPreDiscountPrice() {
		return this.stream()
				.map(Priced::getPreDiscountPrice)
				.reduce(MonetaryAmount::add)
				.orElse(Money.of(0, EURO));
	}

}

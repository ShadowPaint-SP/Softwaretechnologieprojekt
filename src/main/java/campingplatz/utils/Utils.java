package campingplatz.utils;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

public class Utils {

	// hide constructor
	private Utils() {
	}

	// some bullshittery to circumvent the fact that you cannot instantiate generic
	// classes
	public static <T> T createInstance(Class<T> cls) {
		try {
			return cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}


	public static <T extends Priced> MonetaryAmount getPrice(List<T> input){
		return input.stream()
			.map(Priced::getPrice)
			.reduce(MonetaryAmount::add)
			.orElse(Money.of(0, EURO));
	}

	public static <T extends Priced> MonetaryAmount getDiscountedPrice(List<T> input){
		return input.stream()
			.map(Priced::getDiscountedPrice)
			.reduce(MonetaryAmount::add)
			.orElse(Money.of(0, EURO));
	}


	public static LocalDateTime min(LocalDateTime first, LocalDateTime second){
		if (first.isBefore(second)){
			return first;
		}
		else {
			return second;
		}
	}

	public static LocalDateTime max(LocalDateTime first, LocalDateTime second){
		if (first.isAfter(second)){
			return first;
		}
		else {
			return second;
		}
	}

}
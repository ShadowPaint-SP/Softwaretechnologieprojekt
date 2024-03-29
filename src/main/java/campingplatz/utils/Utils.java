package campingplatz.utils;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

import javax.money.MonetaryAmount;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.UnsupportedTemporalTypeException;

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

	public static LocalDateTime min(LocalDateTime first, LocalDateTime second) {
		if (first.isBefore(second)) {
			return first;
		} else {
			return second;
		}
	}

	public static LocalDateTime max(LocalDateTime first, LocalDateTime second) {
		if (first.isAfter(second)) {
			return first;
		} else {
			return second;
		}
	}
	public static String formatDate(LocalDateTime date){
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).replace("-", ".");
	}
	public static String formatDateTime(LocalDateTime date){
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).replace("-", ".").replace(" ", " | ");
	}



	public static TemporalAmount unitToTemporalAmount(ChronoUnit intervalUnit) {
		return switch (intervalUnit) {
			case SECONDS, MINUTES, HOURS, DAYS, WEEKS -> Duration.of(1, intervalUnit);
			case MONTHS -> Period.ofMonths(1);
			case YEARS -> Period.ofYears(1);
			default -> throw new UnsupportedTemporalTypeException("Unsupported unit: " + intervalUnit);
		};

	}


	// truncate a string to a length of 255 at max
	public static String clampLength(String input){
		if (input.length() > 255){
			return input.substring(0, 255);
		}
		else {
			return input;
		}
	}

    // round prices less than a zero to zero
    public static Money clampPrice(Money input){
        if (input.isLessThan(Money.of(0, Currencies.EURO))){
            return Money.of(0, Currencies.EURO);
        }
        else {
            return input;
        }
    }
}
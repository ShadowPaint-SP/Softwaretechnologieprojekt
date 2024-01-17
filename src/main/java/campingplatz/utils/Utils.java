package campingplatz.utils;

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
}
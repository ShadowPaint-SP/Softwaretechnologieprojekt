package campingplatz.utils;

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


	public static TemporalAmount unitToTemporalAmount(ChronoUnit intervalUnit) {
		return switch (intervalUnit) {
			case SECONDS, MINUTES, HOURS, DAYS, WEEKS -> Duration.of(1, intervalUnit);
			case MONTHS -> Period.ofMonths(1);
			case YEARS -> Period.ofYears(1);
			default -> throw new UnsupportedTemporalTypeException("Unsupported unit: " + intervalUnit);
		};

	}
}
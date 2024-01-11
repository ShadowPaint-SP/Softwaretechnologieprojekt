package campingplatz.utils;

import java.time.LocalDateTime;

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

}
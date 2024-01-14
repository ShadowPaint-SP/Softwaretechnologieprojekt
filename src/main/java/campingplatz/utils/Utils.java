package campingplatz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public static String formatDate(LocalDateTime date){
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).replace("-", ".");
	}
	public static String formatDateTime(LocalDateTime date){
		return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).replace("-", ".").replace(" ", " | ");
	}

}
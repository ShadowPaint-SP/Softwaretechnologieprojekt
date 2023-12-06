package campingplatz.utils;

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

}
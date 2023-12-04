package campingplatz.utils;

import campingplatz.reservation.ReservationEntry;
import org.salespointframework.catalog.Product;


public class Utils {

    // hide constructor
    private Utils() {
    }



	// some bullshittery to circumvent the fact that you cannot instantiate generic classes
	public static <T> T createInstance(Class<T> cls){
		try {
			return cls.getDeclaredConstructor().newInstance();
		}
		catch (Exception e){
			return null;
		}
	}



}
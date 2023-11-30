package campingplatz.utils;

import campingplatz.reservation.ReservationEntry;
import com.google.common.reflect.TypeResolver;
import com.google.common.reflect.TypeToken;
import org.salespointframework.catalog.Product;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Utils {

    // hide constructor
    private Utils() {
    }


	public static <T extends Product> int compareReservationEntries(ReservationEntry<T> first, ReservationEntry<T> second) {
		var firstName = first.getProduct().getName();
		var secondName = second.getProduct().getName();
		var nameComparison = firstName.compareTo(secondName);

		if (nameComparison != 0){
			return nameComparison;
		}

		var firstDate = first.getTime();
		var secondDate = second.getTime();

		return firstDate.compareTo(secondDate);
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
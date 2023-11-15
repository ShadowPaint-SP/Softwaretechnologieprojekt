package campingplatz.utils;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.*;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Abstraction of a shopping cart.
 *
 * The api of the Salespoint Cart does not fit our application,
 * so we make our own. It is a simple extension to an ArrayList
 */
public class Cart<T extends Priced> extends ArrayList<T> implements Priced {

    @Override
    public MonetaryAmount getPrice() {
        MonetaryAmount accumulator = Money.of(0, EURO);
        for (var product : this) {
            accumulator = accumulator.add(product.getPrice());
        }
        return accumulator;
    }

}

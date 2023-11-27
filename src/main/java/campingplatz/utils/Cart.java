package campingplatz.utils;

import org.javamoney.moneta.Money;
import javax.money.MonetaryAmount;

import java.util.ArrayList;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Abstraction of a shopping cart.
 * <p>
 * The api of the Salespoint Cart does not fit our application,
 * so we make our own. It is a simple extension to an ArrayList
 * <p>
 * The add method is overridden fuses neighboring reservations
 * of the same plot together, into single bigger reservations.
 * This assumes all added reservations are disjuctive to the
 * reservations in the cart
 * <p>
 * The remove method is overridden to split reservations into smaller
 * ones if nessesary
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
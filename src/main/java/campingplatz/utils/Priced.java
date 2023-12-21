package campingplatz.utils;

import javax.money.MonetaryAmount;

/**
 * Interface for any priced item to ease summing up priced items.
 *
 * The salespoint Prices Interface is private to the order package,
 * so we make our own. It is a simple one function interface
 */
public interface Priced {

    /**
     * Returns the (undiscounted) price of the item.
     */
    MonetaryAmount getPrice();

	/**
	 * Returns the discounted price of the item.
	 */
	default MonetaryAmount getDiscountedPrice(){
		return getPrice();
	}
}

package campingplatz.reservation;

import campingplatz.utils.Cart;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.*;

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
public class ReservationCart extends Cart<Reservation> {

	/**
	 * Adds the given reservation into the cart.
	 * multiple reservations with bordering periods
	 * of the same plot get combined into singular
	 * bigger reservations
	 */
	@Override
	public boolean add(Reservation t) {

		List<Reservation> neighboring = neighboring(t);

		if (neighboring.isEmpty()) {
			return super.add(t);
		}

		LocalDate minArrival = t.getArrival();
		LocalDate maxDeparture = t.getDeparture();
		for (var reservation : neighboring) {
			if (reservation.getArrival().isBefore(minArrival)) {
				minArrival = reservation.getArrival();
			}
			if (reservation.getDeparture().isAfter(maxDeparture)) {
				maxDeparture = reservation.getDeparture();
			}
			super.remove(reservation);
		}

		t.setArrival(minArrival);
		t.setDeparture(maxDeparture);

		return super.add(t);
	}

	/**
	 * removes the given reservation from the cart.
	 * singular reservations might be split by this
	 * removal into multiple smaller reservations
	 */
	public boolean remove(Reservation t) {

		List<Reservation> intersecting = intersecting(t);

		LocalDate minArrival = LocalDate.MAX;
		LocalDate maxDeparture = LocalDate.MIN;
		for (var reservation : intersecting) {
			if (reservation.getArrival().isBefore(minArrival)) {
				minArrival = reservation.getArrival();
			}
			if (reservation.getDeparture().isAfter(maxDeparture)) {
				maxDeparture = reservation.getDeparture();
			}
			super.remove(reservation);
		}

		var leftBorder = t.getArrival().minusDays(1);
		var rightBorder = t.getDeparture().plusDays(1);

		if (!minArrival.isAfter(leftBorder)) {
			var leftReservation = new Reservation(t.getUser(), t.getPlot(), minArrival, t.getArrival().minusDays(1));
			super.add(leftReservation);
		}

		if (!maxDeparture.isBefore(rightBorder)) {
			var rightReservation = new Reservation(t.getUser(), t.getPlot(), rightBorder, maxDeparture);
			super.add(rightReservation);
		}

		return false;

	}

	/**
	 * Gets all reservations in the cart, that are
	 * bordering or overlapping with the given reservation
	 */
	public List<Reservation> neighboring(Reservation t) {
		List<Reservation> neighboring = new ArrayList<>();
		for (var reservation : this) {
			if (reservation.neighbors(t)) {
				neighboring.add(reservation);
			}
		}
		return neighboring;
	}

	/**
	 * Gets all reservations in the cart, that are
	 * overlapping with the given reservation
	 */
	public List<Reservation> intersecting(Reservation t) {
		List<Reservation> intersections = new ArrayList<>();
		for (var reservation : this) {
			if (reservation.intersects(t)) {
				intersections.add(reservation);
			}
		}
		return intersections;
	}

	@Override
	public MonetaryAmount getPrice() {
		MonetaryAmount accumulator = Money.of(0, EURO);
		for (var product : this) {
			accumulator = accumulator.add(product.getPrice());
		}
		return accumulator;
	}

}
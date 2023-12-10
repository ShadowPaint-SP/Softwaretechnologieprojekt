package campingplatz.reservation;

import campingplatz.reservation.Reservation;
import campingplatz.reservation.ReservationEntry;
import campingplatz.utils.Priced;
import campingplatz.utils.Utils;
import one.util.streamex.StreamEx;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import java.time.Duration;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import static org.salespointframework.core.Currencies.EURO;

/**
 * Abstraction of a shopping cart.
 * <p>
 * The api of the Salespoint Cart is not generic, which makes it
 * especially bad for our usage. So we make our own, as a simple
 * extension of ArrayList
 * <p>
 * The Cart stores Registrations by splitting them into several individual
 * RegistrationEntries, representing the smallest unit of Registration.
 * The function getReservations recombines the individual ReservationEntries
 * back into Reservations
 */
public class Cart<T extends Product, U extends Reservation<T>> extends ArrayList<ReservationEntry<T>> implements Priced {

	Class<U> reservationType;

	public Cart(Class<U> reservationType) {
		this.reservationType = reservationType;
	}

	public List<U> getReservationsOfUser(UserAccount user) {

		// sort the cart by the name of its elements first and date second.
		this.sort(ReservationEntry::compareTo);

		// groups all consecutive ReservationEntries belonging to the same reservation
		// together
		// if two entries have differing products, they can not be of the same
		// reservation
		// if the two times are more than smallestunit apart, they can not be of the
		// same reservation
		var stream = StreamEx.of(this.stream());
		var groupedReservationEntries = stream.groupRuns((first, second) -> {
			var firstName = first.getProduct().getName();
			var secondName = second.getProduct().getName();
			var equalNames = firstName.equals(secondName);
			if (!equalNames) {
				return false;
			}

			Reservation<T> reservation = Utils.createInstance(reservationType);
			var intervall = Duration.of(1, reservation.getIntervalUnit());
			var firstTime = first.getTime().plus(intervall);
			var secondTime = second.getTime();
			var sameReservation = !firstTime.isBefore(secondTime);

			return sameReservation;

		});

		// construct the List of reservations
		var reservations = groupedReservationEntries.map(list -> {
			var firstElement = list.get(0);
			var lastElement = list.get(list.size() - 1);

			U reservation = Utils.createInstance(reservationType);
			reservation.setUser(user);
			reservation.setProduct(firstElement.getProduct());
			reservation.setBegin(firstElement.getTime());
			reservation.setEnd(lastElement.getTime());

			return reservation;
		});

		// collect from stream into ArrayList
		return reservations.collect(Collectors.toCollection(ArrayList::new));
	}

	// convenience function, for adding whole Reservations into the Cart at once
	public boolean add(Reservation<T> reservation) {

		var prod = reservation.getProduct();
		var begin = reservation.getBegin();
		var end = reservation.getEnd();

		var len = begin.until(end, reservation.getIntervalUnit()) + 1;
		for (int i = 0; i < len; i++) {
			var time = begin.plus(i, reservation.getIntervalUnit());
			var reservationEntry = new ReservationEntry<>(prod, time);
			this.add(reservationEntry);
		}

		return true;
	}

	@Override
	public Iterator<ReservationEntry<T>> iterator() {
		return super.iterator();
	}

	@Override
	public int size() {
		return super.size();
	}

	@Override
	public MonetaryAmount getPrice() {


		MonetaryAmount acuumulator = Money.of(0, EURO);
		for (var entry : this) {
			acuumulator = acuumulator.add(entry.getProduct().getPrice());
		}

		return acuumulator;
	}
}
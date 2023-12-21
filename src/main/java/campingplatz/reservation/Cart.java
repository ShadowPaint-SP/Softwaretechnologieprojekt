package campingplatz.reservation;

import campingplatz.utils.Priced;
import campingplatz.utils.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.salespointframework.catalog.Product;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.format.annotation.DateTimeFormat;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Abstraction of a shopping cart.
 * <p>
 * The api of the Salespoint Cart is not generic, which makes it
 * especially bad for our usage. So we make our own. Create specific
 * Carts by inheriting from this class and specifying the Type of the
 * reservation.
 * <p>
 * The Cart stores Registrations by splitting them into several individual
 * RegistrationEntries, representing the smallest unit of a Registration.
 * The function getReservationsOfUser recombines the individual
 * ReservationEntries back into Reservations
 */
public abstract class Cart<T extends Product, U extends Reservation<T>> extends TreeSet<Cart<T, U>.ReservationEntry> {

	@Getter
	@Setter
	UserAccount user;

	Class<U> reservationType; // unfortunately needed to create an instance of U



	public Cart(Class<U> reservationType) {
		this.reservationType = reservationType;
	}

	public void addEntry(T product, LocalDateTime time) {
		super.add(new ReservationEntry(product, time));
	}

	public void removeEntry(T product, LocalDateTime time) {
		super.remove(new ReservationEntry(product, time));
	}

	public boolean containsEntry(T product, LocalDateTime time) {
		return super.contains(new ReservationEntry(product, time));
	}

	public List<U> getReservations() {

		if (user == null){
			throw new NullPointerException("user is null, because it was not set with setUser");
		}

		// remember the ReservationEntries are sorted, as this is a sorted
		// set. They are compared by name first and time second

		// groups all consecutive ReservationEntries belonging to the same
		// reservation together. If two entries have differing products, they
		// can not be of the same reservation. If the two times are more than
		// the smallest unit apart, they can not be of the same reservation
		var stream = StreamEx.of(this.stream());
		var groupedReservationEntries = stream.groupRuns((first, second) -> {
			var firstName = first.getProduct().getName();
			var secondName = second.getProduct().getName();
			var equalNames = firstName.equals(secondName);
			if (!equalNames) {
				return false;
			}

			U reservation = Utils.createInstance(reservationType);
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
	public boolean add(U reservation) {

		var prod = reservation.getProduct();
		var begin = reservation.getBegin();
		var end = reservation.getEnd();

		var len = begin.until(end, reservation.getIntervalUnit()) + 1;
		for (int i = 0; i < len; i++) {
			var time = begin.plus(i, reservation.getIntervalUnit());
			this.addEntry(prod, time);
		}

		return true;
	}

	// convenience function, for removing whole Reservations into the Cart at once
	public boolean remove(U reservation) {

		var prod = reservation.getProduct();
		var begin = reservation.getBegin();
		var end = reservation.getEnd();

		var len = begin.until(end, reservation.getIntervalUnit()) + 1;
		for (int i = 0; i < len; i++) {
			var time = begin.plus(i, reservation.getIntervalUnit());
			this.removeEntry(prod, time);
		}

		return true;
	}

	// convenience function, for removing whole Reservations into the Cart at once
	public boolean contains(U reservation) {

		var prod = reservation.getProduct();
		var begin = reservation.getBegin();
		var end = reservation.getEnd();

		var len = begin.until(end, reservation.getIntervalUnit()) + 1;
		for (int i = 0; i < len; i++) {
			var time = begin.plus(i, reservation.getIntervalUnit());
			if (!this.containsEntry(prod, time)) {
				return false;
			}

		}

		return true;
	}

	@EqualsAndHashCode
	public class ReservationEntry implements Comparable<Cart<T, U>.ReservationEntry> {

		@Getter
		@Setter
		private T product;

		@Getter
		@Setter
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDateTime time;

		public ReservationEntry() {

		}

		public ReservationEntry(T product, LocalDateTime time) {
			this.product = product;
			this.time = time;
		}

		public int compareTo(ReservationEntry second) {
			var firstName = this.getProduct().getName();
			var secondName = second.getProduct().getName();
			var nameComparison = firstName.compareTo(secondName);

			if (nameComparison != 0) {
				return nameComparison;
			}

			var firstDate = this.getTime();
			var secondDate = second.getTime();

			return firstDate.compareTo(secondDate);
		}

	}
}
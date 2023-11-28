package campingplatz.utils;

import campingplatz.reservation.Reservable;
import campingplatz.reservation.Reservation;
import campingplatz.reservation.ReservationEntry;
import one.util.streamex.StreamEx;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

import java.time.Duration;
import java.util.ArrayList;


import java.util.List;
import java.util.stream.Collectors;

import org.salespointframework.useraccount.UserAccount;
import static org.salespointframework.core.Currencies.EURO;

/**
 * Abstraction of a shopping cart.
 * <p>
 * The api of the Salespoint Cart is not generic, which makes it
 * especially bad for our useage. So we make our own, as a simple
 * extension of ArrayList
 * <p>
 * The Cart stores Registrations by splitting them into several individual
 * RegistrationEntries, representing the smallest unit of Registration.
 * The function getReservations recombines the individual ReservationEntries
 * back into Reservations
 */
public class Cart<T extends Reservable> extends ArrayList<ReservationEntry<T>> implements Priced {


	public List<Reservation<T>> getReservations(UserAccount user){

		// sort the cart by the name of its elements first and date second.
		sort(Utils::compareReservationEntries);

		// groups all consecutive ReservationEntries belonging to the same reservation together
		// if two entries have differing products, they can not be of the same reservation
		// if the two times are more than smallestunit apart, they can not be of the same reservation
		var stream = StreamEx.of(this.stream());
		var groupedReservationEntries = stream.groupRuns((first, second) -> {
			var firstName = first.getProduct().getName();
			var secondName = second.getProduct().getName();
			var equalNames = firstName.equals(secondName);
			if (!equalNames){
				return false;
			}

			var intervall = Duration.of(1, T.getIntervalUnit());
			var firstTime = first.getTime().plus(intervall);
			var secondTime = second.getTime();
			var sameReservation = !firstTime.isAfter(secondTime);

			return sameReservation;

		});

		// construct the List of reservations
		var reservations = groupedReservationEntries.map(list -> {
			var firstElement = list.get(0);
			var lastElement = list.get(list.size() - 1);

			return new Reservation<>(
				user,
				firstElement.getProduct(),
				firstElement.getTime(),
				lastElement.getTime()
			);

		});

		// collect from stream into ArrayList
		return reservations.collect(Collectors.toCollection(ArrayList::new));
	}


	// convenience function, for adding whole Reservations into the Cart at once
	public boolean add(Reservation<T> ReservationEntry) {
		return false;
	}







	@Override
	public MonetaryAmount getPrice() {
		MonetaryAmount accumulator = Money.of(0, EURO);
		for (var reservationEntry : this) {
			accumulator = accumulator.add(reservationEntry.getProduct().getPrice());
		}
		return accumulator;
	}
}
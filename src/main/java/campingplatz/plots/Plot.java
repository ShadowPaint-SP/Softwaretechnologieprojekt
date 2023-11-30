package campingplatz.plots;


import campingplatz.utils.StaticOverride;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;
import org.javamoney.moneta.Money;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;


@Entity
public class Plot extends Product {

	@Getter @Setter
    private Double size; // in square meters

	@Getter @Setter
    private ParkingLot parking;

    public Plot(String name, Double size, Money price, ParkingLot parking) {

        super(name, price);

        this.size = size;
        this.parking = parking;

    }

    @SuppressWarnings({ "unused", "deprecation" })
    public Plot() {
	}



    // a second getter for in inherited field price. returns a formatted String
    public String getPriceString() {
        return getPrice().getNumber().toString() + " Euro";
    }


	// a second getter for size. returns a formatted String
    public String getSizeString() {
        return getSize() + " m²";
    }




    public enum ParkingLot {
        NONE(0, "catalog.parking.no"),
        BIKE_PARKING(1, "catalog.parking.bike"),
        CAR_PARKING(2, "catalog.parking.car"),
        CAMPER_PARKING(3, "catalog.parking.camper");

        public final Integer size;
        public final String label;

		ParkingLot(Integer size, String label) {
            this.size = size;
            this.label = label;
        }

    }

}

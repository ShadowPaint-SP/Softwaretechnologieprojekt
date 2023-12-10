package campingplatz.plots;

import campingplatz.utils.DetailedProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Plot extends DetailedProduct {

    private Double size; // in square meters

    private ParkingLot parking;

	@Getter
	@Setter
	@Embedded
	private Condition condition;







    public Plot(String name, Double size, Money price, ParkingLot parking, String imagePath, String description) {

        super(name, price, imagePath, description);

        this.size = size;
        this.parking = parking;

    }

    public Plot() {
    }

    // a second getter for in inherited field price. returns a double
    public Double getPriceDouble() {
        return getPrice().getNumber().doubleValue();
    }

    // a third getter for in inherited field price. returns a formatted String
    public String getPriceString() {
        return getPriceDouble().toString() + " Euro";
    }

    // a second getter for size. returns a formatted String
    public String getSizeString() {
        return getSize() + " m²";
    }

    public enum ParkingLot {
        NONE(0, "plot.parking.no"),
        BIKE_PARKING(1, "plot.parking.bike"),
        CAR_PARKING(2, "plot.parking.car"),
        CAMPER_PARKING(3, "plot.parking.camper");

        public final Integer size;
        public final String label;

        ParkingLot(Integer size, String label) {
            this.size = size;
            this.label = label;
        }

        public static ParkingLot fromNumber(Integer i) {
            return switch (i) {
                case 0 -> NONE;
                case 1 -> BIKE_PARKING;
                case 2 -> CAR_PARKING;
                case 3 -> CAMPER_PARKING;
                default -> NONE;
            };

        }

    }


	@Embeddable
	public static class Condition {
		public enum State {
			OPERATIONAL,
			DEFECTIVE,
			DELETED
		}
		private Boolean deleted = false;

		@Getter
		@Setter
		@OneToMany
		private List<Issue> issueList;
		public State getState(){
            if (deleted){
                return State.DELETED;
			}
            else if (!issueList.isEmpty()){
                return State.DEFECTIVE;
            }
			else {
				return State.OPERATIONAL;
			}
		}



	}



}

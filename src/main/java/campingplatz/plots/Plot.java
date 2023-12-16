package campingplatz.plots;

import campingplatz.utils.DetailedProduct;
import campingplatz.utils.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Plot extends DetailedProduct {

    private Double size; // in square meters

    private ParkingLot parking;

    @Getter
    @Setter
    private State state = State.OPERATIONAL;

    @Getter
    @Setter
    @OneToMany
    private List<Issue> issueList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

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

    public enum State {
        OPERATIONAL(0, "state.ok"),
        DEFECTIVE(1, "state.broke"),
        HIDDEN(2, "state.deleted");

        public final Integer index;
        public final String label;

        State(Integer index, String label) {
            this.index = index;
            this.label = label;
        }

        public static State fromNumber(Integer i) {
            return switch (i) {
                case 0 -> OPERATIONAL;
                case 1 -> DEFECTIVE;
                case 2 -> HIDDEN;
                default -> OPERATIONAL;
            };

        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public Iterable<Comment> getComments() {
        return comments;
    }

}

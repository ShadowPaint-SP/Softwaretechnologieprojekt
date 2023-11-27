package campingplatz.equip;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;

import java.time.LocalDate;

@Entity
public class SportsEquipment extends Product {
    @SuppressWarnings({ "unused", "deprecation" })
    public SportsEquipment() {
    }

    public enum Status {
        AVAILABLE("Frei"), UNAVAILABLE("Beschäftigt");

        private final String mainState;

        Status(String status) {
            this.mainState = status;
        }

        @Override
        public String toString() {
            return mainState;
        }
    }

    public enum Category {
        VOLLEYBALL_NETS(" Volleyballnetze"),
		BALLS("Bälle"),
		BADMINTON_SETS("Federballsets"),
        TABLE_TENNIS_SETS("Tischtennisgarnituren");

        private final String sportName;

        Category(String sportName) {
            this.sportName = sportName;
        }

        @Override
        public String toString() {
            return sportName;
        }
    }

    private Category category;
    private Status availability;
    private Quantity amount;

    private LocalDate startDate;
    private LocalDate endDate;

    // the price is Leihgebuehr
    public SportsEquipment(String name, Category category, Money price, Quantity amount, Status availability) {

        super(name, price);
        this.category = category;
        this.amount = amount;
        this.availability = availability;

    }

    public String getPriceString() {
        return getPrice().getNumber().toString() + " Euro";
    }

    public Status getAvailability() {
        return availability;
    }

    public Category getCategory() {
        return category;
    }

    public int getAmountInteger() {
        return amount.getAmount().intValue();
    }

    // if the equipment is unavailable
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}

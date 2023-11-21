package campingplatz.equip;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
class EquipCatalogDataInitializer implements DataInitializer {

    private final EquipCatalog equipCatalog;

    EquipCatalogDataInitializer(EquipCatalog equipCatalog) {

        this.equipCatalog = equipCatalog;
    }

    @Override
    public void initialize() {

        equipCatalog.save(new SportsEquipment(
                "Sportgerät 1",
                SportsEquipment.Category.VOLLEYBALL_NETS,
                Money.of(10, EURO),
                Quantity.of(10),
                SportsEquipment.Status.AVAILABLE));
        equipCatalog.save(new SportsEquipment(
                "Sportgerät 2",
                SportsEquipment.Category.BALLS,
                Money.of(5, EURO),
                Quantity.of(10),
                SportsEquipment.Status.UNAVAILABLE));
        equipCatalog.save(new SportsEquipment(
                "Sportgerät 3",
                SportsEquipment.Category.BADMINTON_SETS,
                Money.of(10, EURO),
                Quantity.of(10),
                SportsEquipment.Status.AVAILABLE));
        equipCatalog.save(new SportsEquipment(
                "Sportgerät 4",
                SportsEquipment.Category.TABLE_TENNIS_SETS,
                Money.of(10, EURO),
                Quantity.of(10),
                SportsEquipment.Status.AVAILABLE));

    }
}

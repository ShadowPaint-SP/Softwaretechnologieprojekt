package campingplatz.equip;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.util.Streamable;

public interface EquipCatalog extends Catalog<SportsEquipment> {

    Streamable<SportsEquipment> findByAvailability(SportsEquipment.Status status);
}

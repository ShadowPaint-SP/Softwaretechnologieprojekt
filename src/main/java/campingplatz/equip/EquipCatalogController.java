package campingplatz.equip;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class EquipCatalogController {

    private final EquipCatalog catalog;

    EquipCatalogController(EquipCatalog catalog) {

        this.catalog = catalog;
    }

    @GetMapping("/equipment")
    String catalog(Model model) {

        Streamable<SportsEquipment> availableEquipment = catalog.findByAvailability(SportsEquipment.Status.AVAILABLE);

        long availableCount = availableEquipment.stream().count();

        model.addAttribute("catalog", catalog.findAll());
        model.addAttribute("sportEquipment", availableEquipment);
        model.addAttribute("availableCount", availableCount);
        return "equipcatalog";
    }

}

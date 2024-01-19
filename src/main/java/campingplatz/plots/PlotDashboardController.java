package campingplatz.plots;

import campingplatz.accounting.PlotRepairAccountancyEntry;
import campingplatz.utils.Utils;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.catalog.Product;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class PlotDashboardController {

	PlotCatalog plotCatalog;
    Accountancy accountancy;

	PlotDashboardController(PlotCatalog plotCatalog, Accountancy accountancy) {

		this.plotCatalog = plotCatalog;
        this.accountancy = accountancy;

	}

	@GetMapping("/management/plots")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String plots(Model model) {
		Streamable<Plot> all = plotCatalog.findAll();
		model.addAttribute("plots", all);
		return "dashboards/plot_management";
	}

	@PostMapping("/management/plots/updatePlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String changePlotDetails(Model model, @Valid PlotInformation info) {

		Optional<Plot> plotOptional = plotCatalog.findById(info.getPlotID());
		if (plotOptional.isPresent()) {
			Plot plot = plotOptional.get();
			plot.setName(Utils.clampLength(info.getName()));
			plot.setSize(info.getSize());
			plot.setParking(Plot.ParkingLot.fromNumber(info.getParkingValue()));
			plot.setPrice(Utils.clampPrice(Money.of(info.getPrice(), EURO)));
			plot.setImagePath(Utils.clampLength(info.getPicture()));
			plot.setDesc(Utils.clampLength(info.getDescription()));

            // if the state is null the plot was repaired
            if (info.getState() == null){
                accountancy.add(
                    new PlotRepairAccountancyEntry(info.getRepairCost(), plot)
                );
                plot.setState(Plot.State.OPERATIONAL);
            }
            else {
                plot.setState(Plot.State.fromNumber(info.getState()));
            }

            // dont forget to save
			plotCatalog.save(plot);

			Streamable<Plot> all = plotCatalog.findAll();
			model.addAttribute("plots", all);
		}

		return "dashboards/plot_management";
	}

	@PostMapping("/management/plots/createPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String createPlot(Model model, @Valid PlotInformation info) {

		var plot = new Plot(
				info.getName(),
				info.getSize(),
				Money.of(info.getPrice(), EURO),
				Plot.ParkingLot.fromNumber(info.getParkingValue()),
				info.getPicture(),
				info.getDescription());

		// dont forget to save
		plotCatalog.save(plot);

		Streamable<Plot> all = plotCatalog.findAll();
		model.addAttribute("plots", all);
		return "dashboards/plot_management";
	}

	interface PlotInformation {

		Product.ProductIdentifier getPlotID();

		String getName();

		Double getSize();

		Integer getParkingValue();

		Double getPrice();

		String getDescription();

		String getPicture();

		Integer getState();

        Double getRepairCost();
	}
}

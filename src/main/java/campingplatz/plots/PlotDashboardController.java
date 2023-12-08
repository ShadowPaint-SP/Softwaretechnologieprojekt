package campingplatz.plots;

import campingplatz.customer.Customer;
import campingplatz.customer.CustomerDashboardController;
import campingplatz.reservation.ReservationRepository;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.money.MonetaryAmount;
import java.util.UUID;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class PlotDashboardController {

	PlotCatalog plotCatalog;

	PlotDashboardController(PlotCatalog plotCatalog) {
		this.plotCatalog = plotCatalog;
	}

	@GetMapping("/management/plots")
	@PreAuthorize("hasRole('BOSS')")
	String plots(Model model) {
		Streamable<Plot> all = plotCatalog.findAll();
		model.addAttribute("plots", all);
		return "dashboards/plot_management";
	}

	@PostMapping("/management/plots/updatePlot")
	@PreAuthorize("hasRole('BOSS')")
	String changePlotDetails(Model model, @Valid PlotInformation info) {

		var uuid = info.getPlotID();
		var plot = plotCatalog.findById(uuid).get();

		plot.setName(info.getName());
		plot.setSize(info.getSize());
		plot.setParking(Plot.ParkingLot.fromNumber(info.getParkingValue()));
		plot.setPrice(Money.of(info.getPrice(), EURO));

		// dont forget to save
		plotCatalog.save(plot);

		Streamable<Plot> all = plotCatalog.findAll();
		model.addAttribute("plots", all);
		return "dashboards/plot_management";
	}

	@PostMapping("/management/plots/createPlot")
	@PreAuthorize("hasRole('BOSS')")
	String createPlot(Model model, @Valid PlotInformation info) {

		var plot = new Plot(
			info.getName(),
			info.getSize(),
			Money.of(info.getPrice(), EURO),
			Plot.ParkingLot.fromNumber(info.getParkingValue()),
			"",
			""
		);

		// dont forget to save
		plotCatalog.save(plot);

		Streamable<Plot> all = plotCatalog.findAll();
		model.addAttribute("plots", all);
		return "dashboards/plot_management";
	}

	//TODO
	@PostMapping("/management/plots/deletePlot")
	@PreAuthorize("hasRole('BOSS')")
	String deletePlot(Model model, @Valid PlotInformation info) {

		// cannot just delete the entry, reservations might depend on it
		try {
			plotCatalog.deleteById(info.getPlotID());
		}
		catch (Exception e){
			// just continue
		}

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
	}
}

package campingplatz.seasonalplots;

import campingplatz.plots.Plot;
import campingplatz.plots.PlotCatalog;
import jakarta.validation.Valid;
import org.javamoney.moneta.Money;
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
public class SeasonalPlotDashboardController {
	SeasonalPlotCatalog seasonalPlotCatalog;

	SeasonalPlotDashboardController(SeasonalPlotCatalog plotCatalog) {
		this.seasonalPlotCatalog = plotCatalog;
	}

	@GetMapping("/management/seasonalplot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String seasonalPlots(Model model) {
		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	@PostMapping("/management/seasonalplot/updateSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String changeSeasonalPlotDetails(Model model, @Valid SeasonalPlotDashboardController.SeasonalPlotInformation info) {

		Optional<SeasonalPlot> plotOptional = seasonalPlotCatalog.findById(info.getPlotID());
		if (plotOptional.isPresent()) {
			SeasonalPlot plot = plotOptional.get();
			plot.setName(info.getName());
			plot.setSize(info.getSize());
			plot.setParking(Plot.ParkingLot.fromNumber(info.getParkingValue()));
			plot.setPrice(Money.of(info.getPrice(), EURO));
			plot.setElectricityMeter(info.getElectricityMeter());
			plot.setWaterMeter(info.getWaterMeter());
			plot.setImagePath(info.getPicture());
			plot.setDesc(info.getDescription());

			// dont forget to save
			plot.setState(Plot.State.fromNumber(info.getState()));

			seasonalPlotCatalog.save(plot);

			Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
			model.addAttribute("seasonalPlots", all);
		}

		return "dashboards/seasonalplot_management";
	}

	@PostMapping("/management/seasonalplot/createSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String createSeasonalPlot(Model model, @Valid SeasonalPlotDashboardController.SeasonalPlotInformation info) {

		var splot = new SeasonalPlot(
				info.getName(),
				info.getSize(),
				Money.of(info.getPrice(), EURO),
				Plot.ParkingLot.fromNumber(info.getParkingValue()),
				info.getElectricityMeter(),
				info.getWaterMeter(),
				info.getPicture(),
				info.getDescription());

		// dont forget to save
		seasonalPlotCatalog.save(splot);

		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	// TODO
	@PostMapping("/management/seasonalplot/deleteSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String deleteSeasonalPlot(Model model, @Valid SeasonalPlotDashboardController.SeasonalPlotInformation info) {

		// cannot just delete the entry, reservations might depend on it
		try {
			seasonalPlotCatalog.deleteById(info.getPlotID());
		} catch (Exception e) {
			// just continue
		}

		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
		return "dashboards/seasonalplot_management";
	}

	interface SeasonalPlotInformation {

		Product.ProductIdentifier getPlotID();

		String getName();

		Double getSize();

		Integer getParkingValue();

		Double getElectricityMeter();

		Double getWaterMeter();

		Double getPrice();

		String getDescription();

		String getPicture();

		Integer getState();
	}
}

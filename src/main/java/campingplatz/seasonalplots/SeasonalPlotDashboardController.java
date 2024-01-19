package campingplatz.seasonalplots;

import campingplatz.accounting.PlotRepairAccountancyEntry;
import campingplatz.plots.Plot;
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

/**
 * Contains functions for managing and creating plots.
 */
@Controller
public class SeasonalPlotDashboardController {
	SeasonalPlotCatalog seasonalPlotCatalog;
    Accountancy accountancy;

	SeasonalPlotDashboardController(SeasonalPlotCatalog plotCatalog, Accountancy accountancy) {
		this.seasonalPlotCatalog = plotCatalog;
        this.accountancy = accountancy;
	}

    /**
     * Find all plots.
     */
	@GetMapping("/management/seasonalplot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String seasonalPlots(Model model) {

		Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
		model.addAttribute("seasonalPlots", all);
        model.addAttribute("electricityCosts", Config.getElectricityCosts().getNumber());
        model.addAttribute("waterCosts", Config.getWaterCosts().getNumber());
		return "dashboards/seasonalplot_management";
	}

    /**
     * Can change name, size, price, parking method, meters,
     * picture path, description.
     *
     * @param info  Contains the value for change
     */
	@PostMapping("/management/seasonalplot/updateSeasonalPlot")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String changeSeasonalPlotDetails(Model model, @Valid SeasonalPlotDashboardController.SeasonalPlotInformation info) {

		Optional<SeasonalPlot> plotOptional = seasonalPlotCatalog.findById(info.getPlotID());
		if (plotOptional.isPresent()) {
			SeasonalPlot splot = plotOptional.get();
            splot.setName(Utils.clampLength(info.getName()));
            splot.setSize(info.getSize());
            splot.setParking(Plot.ParkingLot.fromNumber(info.getParkingValue()));
            splot.setPrice(Money.of(info.getPrice(), EURO));
            splot.setElectricityMeter(info.getElectricityMeter());
            splot.setWaterMeter(info.getWaterMeter());
            splot.setImagePath(Utils.clampLength(info.getPicture()));
            splot.setDesc(Utils.clampLength(info.getDescription()));

            // if the state is null the plot was repaired
            if (info.getState() == null){
                accountancy.add(
                        new PlotRepairAccountancyEntry(info.getRepairCost(), splot)
                );
                splot.setState(Plot.State.OPERATIONAL);
            }
            else {
                splot.setState(Plot.State.fromNumber(info.getState()));
            }

			// dont forget to save
			seasonalPlotCatalog.save(splot);

			Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
			model.addAttribute("seasonalPlots", all);
            model.addAttribute("electricityCosts", Config.getElectricityCosts().getNumber());
            model.addAttribute("waterCosts", Config.getWaterCosts().getNumber());
		}

		return "dashboards/seasonalplot_management";
	}

    /**
     * Create a new permanent camper plot.
     *
     * @param info Contains all data for the new plot
     */
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
        model.addAttribute("electricityCosts", Config.getElectricityCosts().getNumber());
        model.addAttribute("waterCosts", Config.getWaterCosts().getNumber());
		return "dashboards/seasonalplot_management";
	}

    /**
     * The fixed costs for electricity and water can be changed.
     *
     * @param costsInfo Contains the new value for water and electricity.
     */
	@PostMapping("/management/seasonalplot/setCosts")
	@PreAuthorize("hasAnyRole('BOSS', 'EMPLOYEE')")
	String changeCosts(Model model, @Valid SeasonalPlotDashboardController.CostsInfo costsInfo) {

		Config.setElectricityCosts(Money.of(costsInfo.getElectricityCosts(), EURO));
		Config.setWaterCosts((Money.of(costsInfo.getWaterCosts(), EURO)));

        Streamable<SeasonalPlot> all = seasonalPlotCatalog.findAll();
        model.addAttribute("seasonalPlots", all);
        model.addAttribute("electricityCosts", Config.getElectricityCosts().getNumber());
        model.addAttribute("waterCosts", Config.getWaterCosts().getNumber());
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

        Double getRepairCost();
	}

    interface CostsInfo {

        Double getElectricityCosts();

        Double getWaterCosts();
    }
}

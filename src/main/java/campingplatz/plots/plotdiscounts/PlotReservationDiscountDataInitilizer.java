package campingplatz.plots.plotdiscounts;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
class PlotReservationDiscountDataInitilizer implements DataInitializer {
	private final PlotReservationDiscountRepository discountRepository;

	PlotReservationDiscountDataInitilizer(PlotReservationDiscountRepository discountRepository) {
		this.discountRepository = discountRepository;
	}

	@Override
	public void initialize() {
		discountRepository.save(new PlotReservationDiscount(3, 0.20d));
		discountRepository.save(new PlotReservationDiscount(4, 0.30d));
	}
}

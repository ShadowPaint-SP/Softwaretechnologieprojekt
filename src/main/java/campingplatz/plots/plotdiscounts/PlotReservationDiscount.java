package campingplatz.plots.plotdiscounts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static java.lang.Math.*;

@Entity
public class PlotReservationDiscount {

	@Id
	@Getter
	private UUID id;

	@Getter
	@Setter
	private Integer amount;

	@Getter
	private Double discount;


	public PlotReservationDiscount(){
		id = UUID.randomUUID();
	}

	public PlotReservationDiscount(Integer amount, Double discount) {
		id = UUID.randomUUID();
		this.amount = amount;
		this.discount = discount;
	}

	public Long getDiscountPercent(){
		return round(discount * 100);
	}

	public void setDiscountPercent(Integer discountPercent) {
		var discount = ((double) discountPercent / 100);
		this.setDiscount(discount);
	}

	public void setDiscount(double discount) {
		var clampedDiscount = min(1, max(0, discount));
		this.discount = clampedDiscount;
	}
}

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

	// getter below
	// setter below
	private Double discount;


	public PlotReservationDiscount(){
		id = UUID.randomUUID();
	}

	public PlotReservationDiscount(Integer amount, Double discount) {
		id = UUID.randomUUID();
		this.amount = amount;
		this.discount = discount;
	}


	public Long getDiscount() {
		return round(this.discount * 100);
	}

	public void setDiscount(Integer discountPercent) {
		var discountMultiplier = ((double) discountPercent / 100);
		var clampedDiscount = min(1, max(0, discountMultiplier));
		this.discount = clampedDiscount;
	}
}

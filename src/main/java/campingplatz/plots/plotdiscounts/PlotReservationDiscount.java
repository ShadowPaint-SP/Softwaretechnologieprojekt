package campingplatz.plots.plotdiscounts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.UUID;

@Entity
public class PlotReservationDiscount {

	@Id
	@Getter
	private UUID id;

	public PlotReservationDiscount(){
		id = UUID.randomUUID();
	}

}

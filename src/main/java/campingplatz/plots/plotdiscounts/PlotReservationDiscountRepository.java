package campingplatz.plots.plotdiscounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlotReservationDiscountRepository extends CrudRepository<PlotReservationDiscount, UUID> {

	@Override
	List<PlotReservationDiscount> findAll();

	List<PlotReservationDiscount> findAllByAmount(Integer amount);

}

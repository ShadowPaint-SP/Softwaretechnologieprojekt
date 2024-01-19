package campingplatz.plots.plotreservations;

import campingplatz.customer.CustomerManagement;
import campingplatz.plots.PlotCatalog;
import campingplatz.reservation.Reservation;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(30)
class PlotReservationDataInitializer implements DataInitializer {
    private final PlotReservationRepository reservationRepository;
    private final CustomerManagement customerManagement;
    private final PlotCatalog plotCatalog;

    PlotReservationDataInitializer(
            PlotReservationRepository reservationRepository,
            CustomerManagement customerManagement,
            PlotCatalog plotCatalog) {
        this.reservationRepository = reservationRepository;
        this.customerManagement = customerManagement;
        this.plotCatalog = plotCatalog;
    }

    @Override
    public void initialize() {
        var userAccount = customerManagement.findAll().stream().findFirst().get().getUserAccount();
        var currentDay = LocalDate.now().atStartOfDay();

        var plot3 = plotCatalog.findByName("Platz am See I").stream().findFirst().get();
        var arrival3 = currentDay.minusDays(4);
        var departure3 = currentDay.minusDays(0);
        reservationRepository.save(new PlotReservation(userAccount, plot3, arrival3, departure3));
        var newState = Reservation.State.PAYED;
        reservationRepository.findAll().iterator().next().setState(newState);


        var plot1 = plotCatalog.findByName("Platz im Wald I").stream().findFirst().get();
        var arrival1 = currentDay.plusDays(5);
        var departure1 = currentDay.plusDays(6);
        reservationRepository.save(new PlotReservation(userAccount, plot1, arrival1, departure1));

        var plot2 = plotCatalog.findByName("Platz im Wald II").stream().findFirst().get();
        var arrival2 = currentDay.plusDays(0);
        var departure2 = currentDay.plusDays(4);
        reservationRepository.save(new PlotReservation(userAccount, plot2, arrival2, departure2));

        var plot4 = plotCatalog.findByName("Platz am See II").stream().findFirst().get();
        var arrival4 = currentDay.plusDays(2);
        var departure4 = currentDay.plusDays(5);
        reservationRepository.save(new PlotReservation(userAccount, plot4, arrival4, departure4));

        var plot7 = plotCatalog.findByName("Platz auf dem See").stream().findFirst().get();
        var arrival7_1 = currentDay.plusDays(0);
        var departure7_1 = currentDay.plusDays(1);
        reservationRepository.save(new PlotReservation(userAccount, plot7, arrival7_1, departure7_1));

        var arrival7_2 = currentDay.plusDays(3);
        var departure7_2 = currentDay.plusDays(5);
        reservationRepository.save(new PlotReservation(userAccount, plot7, arrival7_2, departure7_2));
    }
}

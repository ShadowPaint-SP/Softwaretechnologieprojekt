package campingplatz.seasonalplots;

import campingplatz.customer.Customer;
import campingplatz.plots.Plot;

import campingplatz.reservation.Reservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservation;
import campingplatz.seasonalplots.seasonalPlotReservations.SeasonalPlotReservationRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class SeasonalPlotReservationUnitTest {

    @Autowired
    private SeasonalPlotReservationRepository reservationRepository;

    @Autowired
    private SeasonalPlotCatalog plotCatalog;

    @Autowired
    private UserAccountManagement userAccountManagement;

    private SeasonalPlotReservation reservation;
    private SeasonalPlotReservation takenReservation;

    private UserAccount customer;
    private SeasonalPlot plot;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reservationRepository.deleteAll();

        customer = userAccountManagement.create("customer", Password.UnencryptedPassword.of("none"),
                Customer.Roles.CUSTOMER.getValue());
        plot = new SeasonalPlot("1. Platz", 15.0, Money.of(20, EURO), Plot.ParkingLot.NONE, 10, 10, "", "");
        plotCatalog.save(plot);

        var arrival = LocalDate.of(2023, 4, 1).atStartOfDay();
        var departure = LocalDate.of(2023, 10, 31).atStartOfDay();

        reservation = new SeasonalPlotReservation(customer, plot, arrival, departure,
                SeasonalPlotReservation.PayMethod.YEARLY);
        reservationRepository.save(reservation);

        takenReservation = new SeasonalPlotReservation(customer, plot, arrival, departure,
                SeasonalPlotReservation.PayMethod.MONTHLY);
        takenReservation.setState(Reservation.State.TAKEN);
        reservationRepository.save(takenReservation);
        reservation.setWaterDifference(20.0);
        reservation.setElectricityDifference(20.0);
    }

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        plotCatalog.delete(plot);
        userAccountManagement.delete(customer);
    }

    @Test
    void init_Reservation() {
        reservation = new SeasonalPlotReservation(customer, plot,
                LocalDate.of(2023, 4, 1).atStartOfDay(),
                LocalDate.of(2023, 10, 31).atStartOfDay(), SeasonalPlotReservation.PayMethod.MONTHLY);
    }

    @Test
    void getMonthsTest() {
        assertEquals(reservation.duration(),
                ChronoUnit.MONTHS.between(LocalDate.of(2023, 4, 1), LocalDate.of(2023, 11, 1)),
                "seasonalReservation.getDays, wrong Days between arrival, departure");
    }

    @Test
    void getPriceTest() {
        assertEquals(
                plot.getPrice()
                        .multiply(ChronoUnit.MONTHS
                                .between(LocalDate.of(2023, 4, 1), LocalDate.of(2023, 11, 1)))
                        .add(Config.getElectricityCosts().multiply(reservation.getElectricityDifference()))
                        .add(Config.getWaterCosts().multiply(reservation.getWaterDifference())),
                reservation.getPrice(),
                "seasonalReservation.getPrice calculate wrong price");
    }

    @Test
    void isNextYearAvaible() {
        assertTrue(reservation.isNextYearAvaible(LocalDate.of(2023, 11, 1).atStartOfDay()), "reservation.isNextYearAvaible, is wrong should be true");
    }

    @Test
    void updateMonthlyPaymentTest() {
        assertEquals(0,reservation.getPayed_months(), "reservation.updateMonthlyPayment, Get payed months but method is yearly");
        reservation.setPayMethod(SeasonalPlotReservation.PayMethod.MONTHLY);
        reservation.setState(Reservation.State.PAYED);
        reservation.updateMonthlyPayment(LocalDateTime.of(2023, 5, 1, 12, 0));
        assertEquals(1, reservation.getPayed_months(),"reservation.updateMonthlyPayment, payed_months should be 1");
    }

    @Test
    void setElectricityDifferenceTest() {
        assertEquals(10.0, reservation.getElectricityDifference(), "reservation.setElectricityDifference, ");
        reservation.setElectricityDifference(8.0);
        assertEquals(10.0, reservation.getElectricityDifference(),"reservation.setElectricityDifference, h");
        reservation.setElectricityDifference(15.0);
        assertEquals(5.0, reservation.getElectricityDifference(), "reservation.setElectricityDifference, ");
    }

    @Test
    void setWaterDifferenceTest() {
        assertEquals(10.0, reservation.getWaterDifference(), "reservation.setElectricityDifference, ");
        reservation.setWaterDifference(8.0);
        assertEquals(10.0, reservation.getWaterDifference(), "reservation.setElectricityDifference, h");
        reservation.setWaterDifference(15.0);
        assertEquals(5.0, reservation.getWaterDifference(), "reservation.setElectricityDifference, ");
    }
}


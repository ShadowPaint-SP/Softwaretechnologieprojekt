package campingplatz.accounting;

import campingplatz.customer.Customer;
import campingplatz.equip.SportItem;
import campingplatz.equip.sportsitemreservations.SportItemReservation;
import campingplatz.plots.Plot;
import campingplatz.plots.plotreservations.PlotReservation;
import campingplatz.seasonalplots.SeasonalPlot;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest
public class AccountingEntryUnitTest {

    private Plot dp;

    private PlotReservation dpr;
    private SeasonalPlot dsp;

    private SportItem sportItem;

    private SportItemReservation sportItemReservation;
    private UserAccount customer;

    @Autowired
    private UserAccountManagement userAccountManagement;

    @BeforeEach
    void setUp() {
        this.customer = userAccountManagement.create("customer", Password.UnencryptedPassword.of("none"),
                Customer.Roles.CUSTOMER.getValue());
        this.dp = new Plot("lol",100.0,
                Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                "","");
        this.dsp = new SeasonalPlot("lol",
                100.0,Money.of(10,EURO), Plot.ParkingLot.BIKE_PARKING,
                100.00,
                100.00,
                "",""
        );
        this.dpr = new PlotReservation(this.customer, this.dp,
                LocalDateTime.of(2000,10,1,1,1),
                LocalDateTime.of(2001,10,1,1,1)
                );
        this.sportItem = new SportItem("lol", Money.of(10, EURO), Money.of(10, EURO),
                "lolo",10,"","");

        this.sportItemReservation = new SportItemReservation(this.customer,this.sportItem,
                LocalDateTime.of(2000,10,1,1,1),
                LocalDateTime.of(2001,10,1,1,1));


    }
    @AfterEach
    void tearDown() {
        userAccountManagement.delete(customer);
    }
    @Test
    public void SeasonalPlotRepairAccountancyEntryConstructorTest() {
        assertDoesNotThrow(() -> {
            new SeasonalPlotRepairAccountancyEntry(10.0, this.dsp);
        }, "SeasonalPlotRepairAccountancyEntry constructor failed");
    }

    @Test
    public void PlotRepairAccountancyEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new PlotRepairAccountancyEntry(10.0, this.dp);
        }, "PlotRepairAccountancyEntry constructor failed");
    }

    @Test
    public void PlotReservationAccountancyEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new PlotReservationAccountancyEntry(this.dpr);
        }, "PlotReservationAccountancyEntry constructor failed");
    }

    @Test
    public void PlotReservationDeductionEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new PlotReservationDeductionEntry(this.dpr);
        }, "PlotReservationAccountancyEntry constructor failed");
    }

    @Test
    public void SportsItemAccountancyEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new SportsItemAccountancyEntry(this.sportItemReservation);
        }, "PlotReservationAccountancyEntry constructor failed");
    }

    @Test
    public void SportsItemDeductionEntryConstructorTest() {

        assertDoesNotThrow(() -> {
            new SportsItemDeductionEntry(this.sportItemReservation);
        }, "PlotReservationAccountancyEntry constructor failed");
    }

}

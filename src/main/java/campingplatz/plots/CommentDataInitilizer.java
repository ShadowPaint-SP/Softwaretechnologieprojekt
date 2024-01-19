package campingplatz.plots;

import campingplatz.customer.Customer;
import campingplatz.customer.CustomerManagement;
import campingplatz.plots.plotreservations.PlotReservationRepository;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.Random;


@Component
@Order(30)
class CommentDataInitializer implements DataInitializer {

    private final PlotReservationRepository reservationRepository;
    private final CustomerManagement customerManagement;
    private final PlotCatalog plotCatalog;

    CommentDataInitializer(
            PlotReservationRepository reservationRepository,
            CustomerManagement customerManagement,
            PlotCatalog plotCatalog) {
        this.reservationRepository = reservationRepository;
        this.customerManagement = customerManagement;
        this.plotCatalog = plotCatalog;
    }


    Integer maxDaysInThePast = 1000;

    LocalDateTime randomTime(){
        LocalDateTime now = LocalDateTime.now();

        Random rand = new Random();
        int randomNum = rand.nextInt(maxDaysInThePast);
        maxDaysInThePast = randomNum;

        return now.minusDays(randomNum).minusSeconds(randomNum);
    }

    Comment createComment(String text, Integer rating, Customer customer){
        return new Comment(
                text, rating, randomTime(),
                customer.getFirstName(), customer.getLastName()
        );
    }


    @Override
    public void initialize() {

        var customers = customerManagement.findAll().toList();
        var customer1 = customers.get(0);
        var customer2 = customers.get(2);
        var customer3 = customers.get(3);
        var customer4 = customers.get(4);

        maxDaysInThePast = 500;
        var plot1 = plotCatalog.findByName("Platz im Wald I").stream().findFirst().get();
        plot1.addComment(createComment(
                "Ich liebe es hier im Wald, unter den hohen Bäumen zu schlafen!",
                5, customer2
        ));
        plot1.addComment(createComment(
                "Zu den Toiletten ist es ein bisschen weit, sonst gut",
                4, customer3
        ));
        plot1.addComment(createComment(
                "In der Natur kommt mir ein wundervolles Gefühl des Seelen Friedens",
                5, customer4
        ));

        maxDaysInThePast = 1000;
        var plot2 = plotCatalog.findByName("Platz am See I").stream().findFirst().get();
        plot2.addComment(createComment(
                "Perfekt zum Fischen",
                5, customer1
        ));
        plot2.addComment(createComment(
                "Wurde von zwei Mücken gestochen",
                2, customer3
        ));

        var plot3 = plotCatalog.findByName("Platz am See II").stream().findFirst().get();
        plot3.addComment(createComment(
                "die anderen Platz an dem See sind besser",
                4, customer1
        ));
        plot3.addComment(createComment(
                "Sehr gute Aussicht für den Preis!",
                5, customer4
        ));


        maxDaysInThePast = 300;
        var plot4 = plotCatalog.findByName("Platz auf dem See").stream().findFirst().get();
        plot4.addComment(createComment(
                "Ich kann es kaum fassen",
                5, customer4
        ));




        plotCatalog.save(plot1);
        plotCatalog.save(plot2);
        plotCatalog.save(plot3);
        plotCatalog.save(plot4);

    }
}
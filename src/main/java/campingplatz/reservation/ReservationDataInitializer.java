/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package campingplatz.reservation;

import campingplatz.customer.CustomerManagement;
import campingplatz.plots.PlotCatalog;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the
 * application on application startup.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see DataInitializer
 */
@Component
@Order(30)
class ReservationDataInitializer implements DataInitializer {
    private final ReservationRepository reservationRepository;
    private final CustomerManagement customerManagement;
    private final PlotCatalog plotCatalog;

    ReservationDataInitializer(
            ReservationRepository reservationRepository,
            CustomerManagement customerManagement,
            PlotCatalog plotCatalog) {
        this.reservationRepository = reservationRepository;
        this.customerManagement = customerManagement;
        this.plotCatalog = plotCatalog;
    }

    @Override
    public void initialize() {
        var userAccount = customerManagement.findAll().stream().findFirst().get().getUserAccount();
        var currentDay = LocalDate.now();

        var plot1 = plotCatalog.findByName("1. Platz").stream().findFirst().get();
        var arrival1 = currentDay.plusDays(5);
        var departure1 = currentDay.plusDays(6);
        reservationRepository.save(new Reservation(userAccount, plot1, arrival1, departure1));

        var plot2 = plotCatalog.findByName("2. Platz").stream().findFirst().get();
        var arrival2 = currentDay.plusDays(0);
        var departure2 = currentDay.plusDays(4);
        reservationRepository.save(new Reservation(userAccount, plot2, arrival2, departure2));

        var plot3 = plotCatalog.findByName("3. Platz").stream().findFirst().get();
        var arrival3 = currentDay.minusDays(4);
        var departure3 = currentDay.minusDays(0);
        reservationRepository.save(new Reservation(userAccount, plot3, arrival3, departure3));

        var plot4 = plotCatalog.findByName("4. Platz").stream().findFirst().get();
        var arrival4 = currentDay.plusDays(2);
        var departure4 = currentDay.plusDays(5);
        reservationRepository.save(new Reservation(userAccount, plot4, arrival4, departure4));

        var plot7 = plotCatalog.findByName("7. Platz").stream().findFirst().get();
        var arrival7_1 = currentDay.plusDays(0);
        var departure7_1 = currentDay.plusDays(1);
        reservationRepository.save(new Reservation(userAccount, plot7, arrival7_1, departure7_1));

        var arrival7_2 = currentDay.plusDays(3);
        var departure7_2 = currentDay.plusDays(5);
        reservationRepository.save(new Reservation(userAccount, plot7, arrival7_2, departure7_2));
    }
}

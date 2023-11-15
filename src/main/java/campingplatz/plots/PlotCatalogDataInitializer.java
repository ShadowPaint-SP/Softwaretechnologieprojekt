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
package campingplatz.plots;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.EURO;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the
 * application on application startup.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see DataInitializer
 */
@Component
@Order(20)
class PlotCatalogDataInitializer implements DataInitializer {
    private final PlotCatalog plotCatalog;

    PlotCatalogDataInitializer(PlotCatalog plotCatalog) {
        this.plotCatalog = plotCatalog;
    }

    @Override
    public void initialize() {
        plotCatalog.save(new Plot("1. Platz", 100.0, Money.of(20, EURO), Plot.ParkingLot.NONE));
        plotCatalog.save(new Plot("2. Platz", 200.0, Money.of(15, EURO), Plot.ParkingLot.BIKE_PARKING));
        plotCatalog.save(new Plot("3. Platz", 300.0, Money.of(35, EURO), Plot.ParkingLot.NONE));
        plotCatalog.save(new Plot("4. Platz", 100.0, Money.of(15, EURO), Plot.ParkingLot.NONE));
        plotCatalog.save(new Plot("5. Platz", 200.0, Money.of(15, EURO), Plot.ParkingLot.CAMPER_PARKING));
        plotCatalog.save(new Plot("6. Platz", 300.0, Money.of(40, EURO), Plot.ParkingLot.CAR_PARKING));
        plotCatalog.save(new Plot("7. Platz", 200.0, Money.of(40, EURO), Plot.ParkingLot.CAMPER_PARKING));
    }
}

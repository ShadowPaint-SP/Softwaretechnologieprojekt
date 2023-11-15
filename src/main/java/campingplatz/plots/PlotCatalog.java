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
import org.salespointframework.catalog.Catalog;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

/**
 * An extension of {@link Catalog} to add specific query methods.
 *
 */
public interface PlotCatalog extends Catalog<Plot> {

    default List<Plot> filter(SiteState query) {
        // we just use filter, instead of specilized database queries.
        // the number of plots in the catalog is not big enough for it to matter
        return findAll().filter(plot -> {
            boolean isMatch = true;

            var priceMin = query.getPriceMin();
            if (priceMin != null && plot.getPrice().isLessThan(Money.of(priceMin, EURO))) {
                isMatch = false;
            }

            var priceMax = query.getPriceMax();
            if (isMatch && priceMax != null && plot.getPrice().isGreaterThan(Money.of(priceMax, EURO))) {
                isMatch = false;
            }

            var sizeMin = query.getSizeMin();
            if (isMatch && sizeMin != null && plot.getSize() < sizeMin) {
                isMatch = false;
            }

            var sizeMax = query.getSizeMax();
            if (isMatch && sizeMax != null && plot.getSize() > sizeMax) {
                isMatch = false;
            }

            var parkingMin = query.getParking();
            if (isMatch && parkingMin != null && plot.getParking().size < parkingMin) {
                isMatch = false;
            }

            return isMatch;

        }).toList();

    }

    // interface representing the state of the plot catalog site
    // most notably the data of the plot filter query
    interface SiteState {

        ////////////////////
        // plot filter query

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getArrival();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getDeparture();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedArrival() {
            if (getArrival() == null) {
                return LocalDate.now();
            }
            return getArrival();
        }

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedDeparture() {
            if (getDeparture() == null) {
                return LocalDate.now().plusDays(1);
            }
            return getDeparture();
        }

        // used in plotcatalog.html for min arrival date.
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate minArrivalDate() {
            return LocalDate.now();
        }

        Double getSizeMin();

        Double getSizeMax();

        Double getPriceMin();

        Double getPriceMax();

        Integer getParking();

        ////////////////////
        // other site data

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate getFirstWeekDate();

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        void setFirstWeekDate(LocalDate value);

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        default LocalDate getDefaultedFirstWeekDate() {
            if (getFirstWeekDate() == null) {
                var weekDay = getDefaultedArrival().getDayOfWeek().getValue() - 1;
                var weekBegin = getDefaultedArrival().minusDays(weekDay);
                return weekBegin;
            }
            return getFirstWeekDate();
        }

    }

}

/*
 * Copyright 2013-2020 the original author or authors.
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

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.salespointframework.catalog.Product;
import org.javamoney.moneta.Money;


@Entity
public class Plot extends Product {

	@Getter @Setter
    private Double size; // in square meters

	@Getter @Setter
    private ParkingLot parking;

    public Plot(String name, Double size, Money price, ParkingLot parking) {

        super(name, price);

        this.size = size;
        this.parking = parking;

    }

    @SuppressWarnings({ "unused", "deprecation" })
    public Plot() {
	}

    // a second getter for in inherited field price. returns a formatted String
    public String getPriceString() {
        return getPrice().getNumber().toString() + " Euro";
    }


	// a second getter for size. returns a formatted String
    public String getSizeString() {
        return getSize() + " m²";
    }




    public enum ParkingLot {
        NONE(0, "catalog.parking.no"),
        BIKE_PARKING(1, "catalog.parking.bike"),
        CAR_PARKING(2, "catalog.parking.car"),
        CAMPER_PARKING(3, "catalog.parking.camper");

        public final Integer size;
        public final String label;

		ParkingLot(Integer size, String label) {
            this.size = size;
            this.label = label;
        }

    }

}

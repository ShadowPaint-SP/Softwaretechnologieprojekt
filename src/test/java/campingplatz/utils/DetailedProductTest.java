package campingplatz.utils;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

class DetailedProductTest {

	private DetailedProduct detailedProduct;
	@BeforeEach
	void setUp(){
		this.detailedProduct = new DetailedProduct("Name", Money.of(10, EURO));
	}
	@Test
	void getAndSetImagePath() {
		String image_path = "sample/path";
		this.detailedProduct.setImagePath(image_path);

		assertEquals(image_path, this.detailedProduct.getImagePath());
	}

	@Test
	void getAndSetDescription() {
		String desc = "sample description";
		this.detailedProduct.setDesc(desc);

		assertEquals(desc, this.detailedProduct.getDesc());
	}


}
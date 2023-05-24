package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
	void containsStockType1() {
	    // given
		ProductType productType = ProductType.HANDMADE;

	    // when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
	void containsStockType2() {
		// given
		ProductType productType = ProductType.BAKERY;

		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
	void containsStockType3() {
		// given
		ProductType productType = ProductType.BOTTLE;

		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isTrue();
	}
}
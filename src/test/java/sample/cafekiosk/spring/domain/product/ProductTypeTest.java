package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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

	// @ParameterizedTest
	@DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
	@CsvSource({"HANDMADE, false", "BOTTLE, true", "BAKERY, true"})
	@ParameterizedTest
	void containsStockType4(
		ProductType productType,
		boolean expected
	) {
		// given

		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isEqualTo(expected);
	}

	@DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
	@MethodSource("provideProductTypesForCheckingStockType")
	@ParameterizedTest
	void containsStockType5(
		ProductType productType,
		boolean expected
	) {
		// given

		// when
		boolean result = ProductType.containsStockType(productType);

		// then
		assertThat(result).isEqualTo(expected);
	}

	private static Stream<Arguments> provideProductTypesForCheckingStockType() {
		return Stream.of(
			Arguments.of(ProductType.HANDMADE, false),
			Arguments.of(ProductType.BOTTLE, true),
			Arguments.of(ProductType.BAKERY, true)
		);
	}
}
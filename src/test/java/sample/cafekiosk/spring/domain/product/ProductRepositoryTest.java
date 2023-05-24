package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/*
	# Persistence Layer
		- Data Access 역할
		- 비즈니스 가공 로직이 포함되어서는 안 된다.
		- Data에 대한 CRUD에만 집중한 레이어
 */
// @DataJpaTest
@SpringBootTest // 통합 테스트
@ActiveProfiles("test")
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
	void findAllBySellingTypeIn() {
	    // given
		Product product1 = Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();
		Product product2 = Product.builder()
			.productNumber("002")
			.type(HANDMADE)
			.sellingType(HOLD)
			.name("카페라떼")
			.price(4500)
			.build();
		Product product3 = Product.builder()
			.productNumber("003")
			.type(HANDMADE)
			.sellingType(STOP_SELLING)
			.name("팥빙수")
			.price(7000)
			.build();
		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		List<Product> products = productRepository.findAllBySellingTypeIn(List.of(SELLING, HOLD));

		// then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingType")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라떼", HOLD)
			);
	}
}
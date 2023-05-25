package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/*
	# Persistence Layer
		- Data Access 역할
		- 비즈니스 가공 로직이 포함되어서는 안 된다.
		- Data에 대한 CRUD에만 집중한 레이어
		- Repository Test
		- 통합 테스트지만 Layer 만 떼서 테스트하는 느낌이라 단위 테스트 같기도 하다.

	# Business Layer
		- 비즈니스 로직을 구현하는 역할
		- Persistence Layer 와의 상호작용을 통해 비즈니스 로직을 전개시킨다.
		- 트랜잭션을 보장해야 한다.
		- Service Test

	# Presentation Layer
		- Controller Test

	---

	# @DataJpaTest
		- JPA 관련 빈만 띄운다.
		- 트랜잭션 어노테이션 제공

	# @SpringBootTest
		- 통합 테스트
		- 트랜잭션 어노테이션을 제공하지 않아서 전체 테스트를 돌릴 때 데이터 중복 이슈가 존재
 */
@DataJpaTest // 트랜잭션 어노테이션 제공
// @SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
	void findAllBySellingTypeIn() {
	    // given
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
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

	@Test
	@DisplayName("상품번호 리스트로 상품들을 조회한다.")
	void findAllByProductNumberIn() {
		// given
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
		productRepository.saveAll(List.of(product1, product2, product3));

		// when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

		// then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingType")
			.containsExactlyInAnyOrder(
				tuple("001", "아메리카노", SELLING),
				tuple("002", "카페라떼", HOLD)
			);
	}

	@Test
	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
	void findLatestProductNumber() {
	    // given
		String targetProductNumber = "003";
		Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
		Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);
		productRepository.saveAll(List.of(product1, product2, product3));

	    // when
		String latestProductNumber = productRepository.findLatestProductNumber();

		// then
		assertThat(latestProductNumber).isEqualTo(targetProductNumber);
	}

	@Test
	@DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때 상품이 하나도 없는 경우 null을 반환한다.")
	void findLatestProductNumberWhenProductIsEmpty() {
		// when
		String latestProductNumber = productRepository.findLatestProductNumber();

		// then
		assertThat(latestProductNumber).isNull();
	}

	private Product createProduct(
		String productNumber,
		ProductType type,
		ProductSellingType sellingType,
		String name,
		int price
	) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingType(sellingType)
			.name(name)
			.price(price)
			.build();
	}
}
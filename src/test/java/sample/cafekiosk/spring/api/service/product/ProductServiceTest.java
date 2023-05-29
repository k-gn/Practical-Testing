package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@BeforeAll
	static void beforeAll() {
		// before class
	}

	@BeforeEach
	void setUp() {
		// before method

		// @BeforeEach 에 위치한 코드들은 테스트간의 결합도가 생기게 만든다. (지양할 것)
		// 스크롤을 위아래로 내리면서 확인해야 해서 문서로서의 테스트 코드 역할이 힘들고 코드를 파악하기 어렵다. (파편화)
		// 그럼 언제 쓰나?
		// 	- 각 테스트 입장에서 봤을 때 아예 몰라도 테스트 내용을 이해하는 데 문제가 없는가?
		// 	- 수정해도 모든 테스트에 영향을 주지 않는가?
	}

	@Test
	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
	void createProduct() {
		/*
			data.sql 을 사용하는 것도 사실 파편화 효과라 지양하면 좋다.
				+ 프로젝트가 커지면 커질수록 양이 많아질 텐데 이렇게 되면 또 다른 관리 포인트가 되어버린다.

		 	그럼 given 데이터가 너무 많아 미리 구성하고 싶다면?
		 		- createProduct 같은 메소드 활용 (테스트 클래스 내에서 필요한 것들만 받아 생성시키는 메소드)
		 		- Test Fixture (createProduct 같은 빌더 메소드를 한 곳에 모아 사용하는 개념)
		 			- 단. 빌더 메소드가 너무 많이 생겨 복잡도가 올라갈 수 있다. (자바 특성이긴 한대 코틀린을 쓰면 해소 되긴 한다)
		 */

	    // given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product);

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.contains("002", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.containsExactlyInAnyOrder(
				tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
				tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
			);
	}

	@Test
	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품 번호는 001 이다.")
	void createProductWhenProductsIsEmpty() {
		// given
		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.contains("001", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.contains(
				tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
			);
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
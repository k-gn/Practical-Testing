package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.controller.order.response.OrderResponse;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

// @Transactional 을 달아도 전체 테스트 돌릴 때 중복 이슈를 해결할 순 있지만 부작용이 있다.
// => 비즈니스 로직에 트랜잭션 문제를 발견하지 못할 수 있음 (서비스에 트랜잭션이 있다고 판단할 수 있다)
// => JPA 같은 경우 트랜잭션 종료 시점에 커밋 / 변경감지가 일어나기 때문에 더욱 주의해야한다.
// => 스프링 배치 통합 테스트 같은 경우 여러 트랜잭션 경계가 있기 때문에 사용하기 어렵다.
// => 이러한 사이드 이펙트를 잘 알고 사용하면 상관 없다.
@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private OrderService orderService;

	@AfterEach
	void tearDown() {
		// deleteAllInBatch 는 순서를 잘 고려해야 한다 (외래키 조건으로 인해 안 지워질 수 있다.)
		orderProductRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("상품번호 리스트를 받아 주문을 생성한다.")
	void createOrder() {
		// given
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();
		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 4000);
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("002", 3000)
			);
	}

	@Test
	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	void createOrderWithDuplicateProductNumbers() {
		// given
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();
		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 2000);
		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("001", 1000)
			);
	}

	@Test
	@DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
	void createOrderWithStock() {
		// given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();
		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 10000);
		assertThat(orderResponse.getProducts()).hasSize(4)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("001", 1000),
				tuple("002", 3000),
				tuple("003", 5000)
			);

		List<Stock> stocks = stockRepository.findAll();
		assertThat(stocks).hasSize(2)
			.extracting("productNumber", "quantity")
			.containsExactlyInAnyOrder(
				tuple("001", 0),
				tuple("002", 1)
			);
	}

	@Test
	@DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
	void createOrderWithNoStock() {
		// given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 1);
		Stock stock2 = Stock.create("002", 2);
		/*
			todo : 테스트의 관심사에서 벗어나기 때문에 재고차감이란 다른 행위를 끌어다 쓰는 것은 지양하자.
				   복잡한 테스트인 경우 이러한 코드때문에 테스트가 깨질 수 있고, 유추하기도 어려워 진다.
				   맥락을 이해하는데 있어서 허들이 하나 생긴 것이다. -> 테스트 환경의 독립성을 보장하자.
				   테스트 환경 구성 시 순수한 빌더나 생성자 기반으로 객체를 만들어 처리하는 것이 좋다.
		 */
		// stock1.deductQuantity(1);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();
		LocalDateTime registeredDateTime = LocalDateTime.now();

		// when & then
		assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("재고가 부족한 상품이 있습니다.");
	}

	private Product createProduct(
		ProductType type,
		String productNumber,
		int price
	) {
		return Product.builder()
			.type(type)
			.productNumber(productNumber)
			.price(price)
			.sellingType(SELLING)
			.name("메뉴 이름")
			.build();
	}
}
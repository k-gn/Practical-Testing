package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.order.OrderStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("해당 일자 및 주문 상태의 주문들을 가져옵니다.")
	void findOrdersBy() {
		// given
		LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59), products);
		Order order2 = createPaymentCompletedOrder(now, products);
		Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5,23, 59), products);
		Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

		// when
		List<Order> orders = orderRepository.findOrdersBy(now, now.plusDays(1), PAYMENT_COMPLETED);

		// then
		assertThat(orders).hasSize(2)
			.extracting("orderStatus", "registeredDateTime", "totalPrice")
			.containsExactlyInAnyOrder(
				tuple(PAYMENT_COMPLETED, order2.getRegisteredDateTime(), order2.getTotalPrice()),
				tuple(PAYMENT_COMPLETED, order3.getRegisteredDateTime(), order3.getTotalPrice())
			);
	}

	private Order createPaymentCompletedOrder(
		LocalDateTime now,
		List<Product> products
	) {
		Order order = Order.builder()
			.products(products)
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registeredDateTime(now)
			.build();
		return orderRepository.save(order);
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
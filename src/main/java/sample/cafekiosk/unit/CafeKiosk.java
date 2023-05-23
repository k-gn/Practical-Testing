package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

/*
	# lombok 사용 가이드
	- @Data, @Setter, @AllArgsConstructor 지양
	- 양방향 연관관계 시 @ToString 순환참조 문제
 */
@Getter
public class CafeKiosk {

	private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
	private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

	private final List<Beverage> beverages = new ArrayList<>();

	public void add(Beverage beverage) {
		beverages.add(beverage);
	}

	public void add(
		Beverage beverage,
		int count
	) {
		if (count <= 0)
			throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");

		for (int i = 0; i < count; i++)
			beverages.add(beverage);
	}

	public void remove(Beverage beverage) {
		beverages.remove(beverage);
	}

	public void clear() {
		beverages.clear();
	}

	/*
		# TDD (레드, 그린, 리팩토링)
		- 복잡도가 낮은 테스트 가능한 코드로 구현할 수 있게 한다.
		- 쉽게 발견하기 어려운 케이스를 놓치지 않게 해준다.
		- 구현에 대한 빠른 피드백을 받을 수 있다.
		- 과감한 리펙토링이 가능해진다.
		- 테스트와 상호작용하며 발전하는 구현부
		- 사용자 관점에서 피드백을 주는 Test Driven 라고 할 수 있다.
	 */
	public int calculateTotalPrice() {
		return beverages.stream().mapToInt(Beverage::getPrice).sum();
	}

	public Order createOrder() {
		LocalDateTime now = LocalDateTime.now();
		LocalTime currentTime = now.toLocalTime();
		if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME))
			throw new IllegalArgumentException("주문 시간이 아닙니다.");

		return new Order(now, beverages);
	}

	public Order createOrder(LocalDateTime localDateTime) {
		LocalTime currentTime = localDateTime.toLocalTime();
		if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME))
			throw new IllegalArgumentException("주문 시간이 아닙니다.");

		return new Order(localDateTime, beverages);
	}
}

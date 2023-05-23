package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

/*
	## 테스트는 "문서" 다.
	- 프로덕션 기능을 설명하는 테스트 코드 문서
	- 다양한 테스트 케이스를 통해 이해하는 시각과 관점을 보완
	- 어느 사람의 과거 경험했던 고민의 결과물을 팀 차원으로 공유할 수 있다.
	- 항상 팀으로 일하기 때문에 코드/문서를 항상 고민하며 작성하자.

	# DisplayName 은 섬세하게
		- 명사의 나열 보다 문장으로 표현하자.
		- "~ 테스트" 지양
		- 테스트 행위에 대한 결과까지 기술하면 좋다.
		- 도메인 용어(도메인 정책)를 사용하여 한층 추상화된 내용을 담자.
			- ex. 특정 시간 이전에 주문을 생성하면 실패한다 -> 영엉 시작 시간 이전에 주문을 생성할 수 없다.
		- 테스트 현상을 중점으로 기술하지 말자 ("~ 실패한다" 이런 워딩 지양)

	# BDD Style
		- 시나리오에 기반한 테스트 케이스 자체에 집중하여 테스트
			- Given : 시나리오 진행에 필요한 준비과정
			- When : 시나리오 행동 진행
			- Then : 시나리도 진행에 대한 결과 검증
		- 개발자가 아닌 사람이 봐도 이해할 수 있을 정도의 추상화 수준을 권장
		- 어떤 환경에서(Given), 어떤 행동을 했을 때(When), 어떤 변화가 일어난다(Then). => DisplayName을 명확하게 작성할 수 있다.
 */
class CafeKioskTest {

	@Test
	@DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
	void add() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages()).hasSize(1);
		assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void calculateTotalPrice() {
		// given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);

		// when
		int totalPrice = cafeKiosk.calculateTotalPrice();

		// then
		assertThat(totalPrice).isEqualTo(americano.getPrice() + latte.getPrice());
	}

	@Test
	void remove() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.remove(americano);
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	@Test
	void clear() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		cafeKiosk.add(americano);
		cafeKiosk.add(latte);
		assertThat(cafeKiosk.getBeverages()).hasSize(2);

		cafeKiosk.clear();
		assertThat(cafeKiosk.getBeverages()).isEmpty();
	}

	/*
		# 테스트 세분화
		- 해피 케이스, 예외 케이스
			- 경계값이 있다면 경계값을 기준으로 테스트
	 */
	@Test
	void addSeveralBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano, 2);

		assertThat(cafeKiosk.getBeverages()).hasSize(2);
		assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
		assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
	}

	@Test
	void addZeroBeverages() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
	}

	@Test
	void createOrder() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		Order order = cafeKiosk.createOrder();

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void createOrderWithCurrentTime() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		/*
			테스트하기 어려운 영역을 구분하고 분리하기
			- 외부로 분리할수록 테스트 가능한 코드는 많아진다.

			테스트하기 어려운 영역
			- 관측할때마다 다른 값에 의존하는 코드
				- 현재날짜/시간, 랜덤값, 전역변수, 사용자 입력 등
			- 외부 세계에 영향을 주는 코드
				- 표준출력, 메시지발송, 데이터베이스에 기록 등

			같은 입력에 항상 같은 결과, 외부 세상과 단절된 형태 -> 테스트하기 쉬운 코드 (순수함수)
		 */
		Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 1, 17, 10, 0));

		assertThat(order.getBeverages()).hasSize(1);
		assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void createOrderOutsideOpenTime() {
		CafeKiosk cafeKiosk = new CafeKiosk();
		Americano americano = new Americano();

		cafeKiosk.add(americano);

		assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 1, 17, 9, 59)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문 시간이 아닙니다.");
	}
}
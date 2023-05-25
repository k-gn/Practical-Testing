package sample.cafekiosk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/*
	# Layered Architecture 도 단점은 존재한다.
		- 도메인 객체라 부르고 있는 엔티티가 외부의 JPA와 뗄 수 없는 강결합된 구조
		- 인프라스트럭처와 강결합 되어있는 것이 맞는가?

	=> 이러한 단점 때문에 대조되고 있는 아키텍처가 Hexagonal Architecture 이다.
		- 도메인 모델을 중심으로 둘러싸고 있는 모양
		- 외부 시스템들이 어댑터(port)를 통해 도메인 모델로 접근한다.
			- 도메인 모델은 아예 외부의 것을 전혀 모른다.
			- ex. 데이터 엑세스를 위한 레포지토리를 따로 두고, 이 레포지토리를 구현하는 JPA 레포지토리를 따로 두고 런타임 시 주입해준다.
		- 멀티모듈로 커지는 시스템일 때 적용하면 좋다.
 */
@SpringBootTest
class CafeKioskApplicationTests {

	@Test
	void contextLoads() {
	}

}

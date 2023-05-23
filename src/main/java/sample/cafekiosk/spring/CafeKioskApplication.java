package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
	- 단위 테스트도 중요하지만 A 기능과 B 기능이 합쳐 동작되었을 때 잘 동작되는지 커버하기 힘들다.
	- 따라서 통합 테스트가 필요하다.
		- 여러 모듈이 협력하는 기능을 통합적으로 검증하는 테스트이다.
		- 일반적으로 작은 범위의 단위 테스트 만으로는 기능 전체의 신뢰성을 보장할 수 없다.
	- 풍부한 단위 테스트 + 큰 기능 단위를 검증하는 통합테스트
 */
@SpringBootApplication
public class CafeKioskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeKioskApplication.class, args);
	}

}

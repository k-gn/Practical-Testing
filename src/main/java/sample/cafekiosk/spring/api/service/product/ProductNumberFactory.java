package sample.cafekiosk.spring.api.service.product;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.domain.product.ProductRepository;

/*
	## private method 는 테스트를 할 필요가 없다.
		- 클라이언트 입장 (사용하는 입장)은 private method 까지 알아야 할 필요가 없다.
		- public method 를 실행하면서 자연스럽게 검증이 된다.
		- 정말 테스트를 해야 한다고 생각이 들면 "객체를 분리해야 하는게 아닌가?" 하는 고민을 먼저하자.
 */
@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

	private final ProductRepository productRepository;

	// 책임 분리 (ProductService -> ProductNumberFactory)
	public String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null)
			return "001";

		int latestProductNumberInt = Integer.parseInt(latestProductNumber);
		int nextProductNumberInt = latestProductNumberInt + 1;
		return String.format("%03d", nextProductNumberInt);
	}
}

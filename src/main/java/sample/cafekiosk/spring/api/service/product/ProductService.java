package sample.cafekiosk.spring.api.service.product;

import static sample.cafekiosk.spring.domain.product.ProductSellingType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/*
	- 아래 키워드에 중요한 구분 역할을 하는 것이 readOnly = true
		- CQRS : Command(CUD) / Read
		- DB Replication (Master / Slave)
	=> 잘 구분을 해주는 것이 중요하다.
 */
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingTypeIn(forDisplay());

		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {
		/*
			- 실제론 ProductNumber 동시성 이슈를 고려해야한다.
				- 유니크 조건 및 재시도 로직
				- 증가값이 아닌 UUID 같은 정책을 활용한 고유값 활용
		 */
		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	private String createNextProductNumber() {
		String latestProductNumber = productRepository.findLatestProductNumber();
		if (latestProductNumber == null)
			return "001";

		int latestProductNumberInt = Integer.parseInt(latestProductNumber);
		int nextProductNumberInt = latestProductNumberInt + 1;
		return String.format("%03d", nextProductNumberInt);
	}
}

package sample.cafekiosk.spring.api.controller.product.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

	@NotNull(message = "상품 타입은 필수입니다.")
	private ProductType type;

	@NotNull(message = "상품 판매상태는 필수입니다.")
	private ProductSellingType sellingType;

	// 어떤 검증을 컨트롤러 단에서 할 건지, 해당 검증이 컨트롤러단 책임이 맞는지 위치를 고려해야 한다.
	// 성격에 따라 다른 레이어에서 검증을 한다.
	// 유효한 문자열인지에 대한 검증과 도메인 정책에 맞는 특수한 형태의 검증을 구분할 줄 알아야 한다.
	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;

	@Positive(message = "상품 가격은 양수여야 합니다.")
	private int price;

	/*
		# 테스트에서만 필요한 메서드가 생겼는데 프로덕션 코드에선 필요가 없다면?
			- 만들어도 되긴 하지만 무분별하게 만들지 말자.
			- 마땅히 가져도 되는 성격의 행위라면 괜찮다.
	 */
	@Builder
	private ProductCreateRequest(
		ProductType type,
		ProductSellingType sellingType,
		String name,
		int price
	) {
		this.type = type;
		this.sellingType = sellingType;
		this.name = name;
		this.price = price;
	}

	public ProductCreateServiceRequest toServiceRequest() {
		return ProductCreateServiceRequest.builder()
			.type(type)
			.sellingType(sellingType)
			.name(name)
			.price(price)
			.build();
	}
}

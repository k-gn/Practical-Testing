package sample.cafekiosk.spring.api.service.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
	# Service Layer 에서 Controller Layer 와의 의존성을 없애기 위해 만든 Clean Request Dto
		- Service Layer 에선 사용하지 않는 Validation 의존성 제거
		- Controller Layer Dto 의 변경이 생겨도 Service Layer 에 영향이 가지 않는다.
 */
@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

	private List<String> productNumbers;

	@Builder
	private OrderCreateServiceRequest(List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}
}

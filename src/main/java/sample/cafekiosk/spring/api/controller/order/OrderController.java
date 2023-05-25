package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.common.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.controller.order.response.OrderResponse;
import sample.cafekiosk.spring.api.service.order.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		LocalDateTime registeredDateTime = LocalDateTime.now();
		return ResponseEntity.ok(ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDateTime)));
	}
}

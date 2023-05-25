package sample.cafekiosk.spring.api.controller.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.common.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/api/v1/products/new")
	public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
		return ResponseEntity.ok(ApiResponse.ok(productService.createProduct(request.toServiceRequest())));
	}

	@GetMapping("/api/v1/products/selling")
	public ResponseEntity<ApiResponse<List<ProductResponse>>> getSellingProduct() {
		return ResponseEntity.ok(ApiResponse.ok(productService.getSellingProducts()));
	}
}

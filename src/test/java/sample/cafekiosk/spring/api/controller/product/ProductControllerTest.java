package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@WebMvcTest(controllers = ProductController.class) // controller layer 관련 bean 을 올려준다.
class ProductControllerTest {

	/*
		- Mock 객체를 사용하여 스프링 MVC 동작을 재현할 수 있는 테스트 프레임워크
	 */
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/*
		- 가짜 객체로 대신해서 테스트하고자 하는 레이어에 집중 (Mocking)
			- 방해되는 의존관계를 가정하여 처리한다.
	 */
	@MockBean
	private ProductService productService;

	@Test
	@DisplayName("신규 상품을 등록한다.")
	void createProduct() throws Exception {
	    // given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

	    // when & then
		mockMvc.perform(post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
	void createProductWithoutType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		mockMvc.perform(post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
	void createProductWithoutSellingType() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		mockMvc.perform(post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
	void createProductWithoutName() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.price(4000)
			.build();

		// when & then
		mockMvc.perform(post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
	void createProductWithZeroPrice() throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(ProductType.HANDMADE)
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(0)
			.build();

		// when & then
		mockMvc.perform(post("/api/v1/products/new")
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("판매 상품을 조회한다.")
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> results = List.of();
		when(productService.getSellingProducts()).thenReturn(results);

		// when & then
		mockMvc.perform(get("/api/v1/products/selling"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())
			.andDo(print());
	}
}
package sample.cafekiosk.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.order.OrderController;
import sample.cafekiosk.spring.api.controller.product.ProductController;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.product.ProductService;

// 테스트 환경 통합하기
@WebMvcTest(controllers = {
	OrderController.class,
	ProductController.class
}) // controller layer 관련 bean 을 올려준다.
public abstract class ControllerTestSupport {

	/*
		- Mock 객체를 사용하여 스프링 MVC 동작을 재현할 수 있는 테스트 프레임워크
	 */
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	/*
		- 가짜 객체로 대신해서 테스트하고자 하는 레이어에 집중 (Mocking)
		- 방해되는 의존관계를 가정하여 처리한다.
	 */
	@MockBean
	protected ProductService productService;

	@MockBean
	protected OrderService orderService;
}

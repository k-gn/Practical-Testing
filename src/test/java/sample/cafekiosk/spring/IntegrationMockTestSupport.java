package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.client.mail.MailSendClient;

// 테스트 환경 통합하기
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationMockTestSupport {

	@MockBean
	private MailSendClient mailSendClient;
}

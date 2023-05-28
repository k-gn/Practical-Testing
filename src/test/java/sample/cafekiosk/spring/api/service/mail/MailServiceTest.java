package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

// 순수 mockito test
@ExtendWith(MockitoExtension.class) // mockito 사용을 인지
class MailServiceTest {

	@Spy // 일부만 stubbing 하고 싶고, 일부는 실제 객체 기능을 실행 / 잘 안씀 (when X / do O)
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	// 한 테스트는 하나의 주제!
	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
	    // given
		// MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
		// MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);
		// MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

		// 행위를 지정하지 않을 경우(no stubbing) 결과로 기본값을 리턴한다.
		when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
			.thenReturn(true);

		// BDD Style
		given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
			.willReturn(true);

		// when
		boolean result = mailService.sendMail("", "", "", "");

		// then
		assertThat(result).isTrue();

		// save 가 1번 호출 되었는지 검증
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}
}

/*
	- mocking 은 꼭 필요한 경우에만 사용하고, 최대한 실제 객체를 사용하여 테스트하자.
	- 런타임 시점에 일어날 일을 정확하게 stubbing 했다고 단언할 수 있는지?
	- 서로 다른 모듈이 연합했을 때 어떤 이슈가 발생할 지 모른다.

	- 우리 시스템 외부 시스템과 연계될 때 mocking 을 주로 사용한다.
 */
package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

		// when
		boolean result = mailService.sendMail("", "", "", "");

		// then
		assertThat(result).isTrue();

		// save 가 1번 호출 되었는지 검증
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}
}
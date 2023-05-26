package sample.cafekiosk.spring.client.mail;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailSendClient {

	public boolean sendEmail(
		String from,
		String to,
		String subject,
		String content
	) {
		// 실제 메일 전송
		log.info("[메일 전송]");
		throw new IllegalArgumentException("메일 전송 실패");
	}
}

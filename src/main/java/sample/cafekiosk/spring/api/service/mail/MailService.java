package sample.cafekiosk.spring.api.service.mail;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
public class MailService {

	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryRepository;

	public boolean sendMail(
		String from,
		String to,
		String subject,
		String content
	) {
		boolean result = mailSendClient.sendEmail(from, to, subject, content);
		if (result) {
			MailSendHistory mailSendHistory = MailSendHistory.builder()
				.fromEmail(from)
				.toEmail(to)
				.subject(subject)
				.content(content)
				.build();
			mailSendHistoryRepository.save(mailSendHistory);
			return true;
		}

		return false;
	}
}

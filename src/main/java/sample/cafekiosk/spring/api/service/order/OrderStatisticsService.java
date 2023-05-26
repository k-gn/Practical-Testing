package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	// 메일전송 같은 긴 작업이 존재하는 서비스에선 트랜잭션을 걸지 않는 것이 좋다.
	public boolean sendOrderStatisticsMail(
		LocalDate orderDate,
		String email
	) {
		// 해당 일자와 상태의 주문들을 가져와서
		List<Order> orders = orderRepository.findOrdersBy(
			orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		// 총 매출 합계를 계산하고
		int totalAmount = orders.stream().mapToInt(Order::getTotalPrice).sum();

		// 메일을 전송한다.
		boolean result = mailService.sendMail(
			"no-reply@cafekiosk.com",
			email,
			String.format("[매출 통계] %s", orderDate),
			String.format("총 매출 합계는 %s원 입니다.", totalAmount)
		);

		if (!result) {
			throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
		}

		return true;
	}
}

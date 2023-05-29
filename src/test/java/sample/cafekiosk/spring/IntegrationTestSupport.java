package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// 테스트 환경 통합하기
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {
}

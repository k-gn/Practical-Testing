package sample.cafekiosk.spring.api.common;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // getter 가 있어야 테스트 코드에서
@NoArgsConstructor
public class ApiResponse<T> {

	private int code;
	private HttpStatus status;
	private String message;
	private T data;

	@Builder
	private ApiResponse(
		HttpStatus status,
		String message,
		T data
	) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(
		HttpStatus status,
		String message,
		T data
	) {
		return ApiResponse.<T>builder()
			.status(status)
			.message(message)
			.data(data)
			.build();
	}

	public static <T> ApiResponse<T> of(
		HttpStatus status,
		T data
	) {
		return of(status, status.name(), data);
	}

	public static <T> ApiResponse<T> ok(T data) {
		return of(HttpStatus.OK, HttpStatus.OK.name(), data);
	}
}

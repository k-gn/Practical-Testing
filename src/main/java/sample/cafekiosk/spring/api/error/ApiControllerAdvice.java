package sample.cafekiosk.spring.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sample.cafekiosk.spring.api.common.ApiResponse;

@RestControllerAdvice // ControllerAdvice + ResponseBody
public class ApiControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ApiResponse<Object> bindException(BindException e) {
		String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return ApiResponse.of(HttpStatus.BAD_REQUEST, defaultMessage, null);
	}
}

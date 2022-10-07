package ec.msplatform.common.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseBusinessException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String correlationId ;

	private HttpStatus httpStatus;


	public BaseBusinessException(String message) {
		super(message);
	}

	public BaseBusinessException(String message, Throwable err) {
		super(message, err);
	}

	public BaseBusinessException(String message,String correlationId) {
		super(message);
		this.correlationId=correlationId;
	}
}

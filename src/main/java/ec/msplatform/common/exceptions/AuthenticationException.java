package ec.msplatform.common.exceptions;

import org.springframework.http.HttpStatus;

import ec.msplatform.common.utilites.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 */
@Getter
@Setter
public class AuthenticationException extends BaseBusinessException {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 *
	 * @param message the exception message
	 */
	public AuthenticationException(String message) {
		super(message);
		setCorrelationId(Utils.getCorrelationId());
	}

	public AuthenticationException(String message, HttpStatus httpStatus) {
		super(message);
		setCorrelationId(Utils.getCorrelationId());
		setHttpStatus(httpStatus);
	}

}

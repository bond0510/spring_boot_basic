package ec.msplatform.common.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ec.msplatform.common.constants.ErrorCodes;
import ec.msplatform.common.constants.Messages;
import ec.msplatform.common.dto.ErrorDetail;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RESTExceptionHandler extends ResponseEntityExceptionHandler{
	/**
	 * Handle ValidationException
	 *
	 * @param ValidationException
	 * @param request
	 * @return the {@link ErrorDetail} object
	 */
	@ExceptionHandler(value = { ValidationException.class })
	public ResponseEntity<ErrorDetail> handleValidationException(final ValidationException validationException,
			final WebRequest request) {
		log.error("Handle NotFoundException : {}", validationException);
		String correlationId = getCorrelationId();
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(getErrorCodeMessage(validationException.getMessage()),
				correlationId, validationException.getMessage()), HttpStatus.BAD_REQUEST);
	}
	/**
	 * Handle HttpMessageNotReadableException
	 *
	 * @param ex
	 * @return the {@link ErrorDetail} object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String correlationId = getCorrelationId();
		return new ResponseEntity<Object>(new ErrorDetail(ex.getMostSpecificCause().getLocalizedMessage(),
				correlationId, ErrorCodes.API_ERROR_001_02.name()), HttpStatus.BAD_REQUEST);

	}

	/**
	 * Handle ConstraintViolationException
	 *
	 * @param ex
	 * @return the {@link ErrorDetail} object
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDetail> handleValidationExceptions(ConstraintViolationException ex) {

		log.error("Handle ConstraintViolationException :{}", ex);
		String correlationId = getCorrelationId();
		List<String> errors = new ArrayList<String>();
		ex.getConstraintViolations().forEach(error -> errors.add(error.getMessage()));

		return new ResponseEntity<ErrorDetail>(
				new ErrorDetail(errors.toString(), correlationId, ErrorCodes.API_ERROR_001_02.name()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle MethodArgumentNotValidException
	 *
	 * @param ex
	 * @return the {@link ErrorDetail} object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<String>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));

		ex.getBindingResult().getGlobalErrors()
				.forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

		String correlationId = getCorrelationId();

		return new ResponseEntity<Object>(
				new ErrorDetail(errors.toString(), correlationId, ErrorCodes.API_ERROR_001_02.name()),
				HttpStatus.BAD_REQUEST);
	}
	/**
	 * Handle all BaseBusinessException
	 * @param baseException
	 * @param request
	 * @return the {@link ErrorDetail} object
	 */
	@ExceptionHandler(value = { BaseBusinessException.class })
	public ResponseEntity<ErrorDetail> handleBusinessException(final BaseBusinessException baseException,
			final WebRequest request) {

		log.error("Handle {} exception :  ", baseException.getClass().getSimpleName(),baseException);
		String correlationId = getCorrelationId();

		return new ResponseEntity<ErrorDetail>(new ErrorDetail(getErrorCodeMessage(baseException.getMessage()),
				correlationId, baseException.getMessage()), baseException.getHttpStatus());

	}
	/**
	 * This method return the correlationId
	 * @return
	 */
	private String getCorrelationId() {
		String correlationId = null;
		if (StringUtils.hasText(MDC.get(Messages.REQUEST_HEADER))) {
			correlationId = MDC.get(Messages.REQUEST_HEADER);
		} else {
			correlationId = UUID.randomUUID().toString().toUpperCase();
		}
		return correlationId;
	}

	/**
	 * This method return the error message for given error code.
	 * @param code
	 * @return
	 */
	public String getErrorCodeMessage(String code) {
		String message = null;
		for (ErrorCodes errorcode : ErrorCodes.values()) {
			if (errorcode.name().equals(code)) {
				message = errorcode.getMessage();
			}
		}
		return message;
	}


}

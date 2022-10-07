package ec.msplatform.common.utilites;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import ec.msplatform.common.constants.ErrorCodes;
import ec.msplatform.common.constants.Messages;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class
 *
 */
public class Utils {

	  private Utils() {

	  }
	  public static String getUUID() {

	    return UUID.randomUUID().toString();
	  }

	  public static String getCorrelationId() {

	    String correlationId = null;
	    if (StringUtils.hasText(MDC.get(Messages.REQUEST_HEADER))) {
	      correlationId = MDC.get(Messages.REQUEST_HEADER);
	    } else {
	      correlationId = getUUID();
	      MDC.put(Messages.REQUEST_HEADER, correlationId);
	    }
	    return correlationId;
	  }

	  public static String getErrorCodeMessage(String code) {

	    String message = null;
	    for (ErrorCodes errorcode : ErrorCodes.values()) {
	      if (errorcode.name().equals(code)) {
	        message = errorcode.getMessage();
	      }
	    }
	    return message;
	  }

	}

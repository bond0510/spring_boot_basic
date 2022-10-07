package ec.msplatform.logging.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoggingService {
	String logRequest(HttpServletRequest httpServletRequest, Object body);

	String logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body);
}

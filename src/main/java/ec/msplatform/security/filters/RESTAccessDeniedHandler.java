package ec.msplatform.security.filters;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.msplatform.common.constants.ErrorCodes;
import ec.msplatform.common.constants.Messages;
import ec.msplatform.common.dto.ErrorDetail;
import ec.msplatform.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RESTAccessDeniedHandler implements AccessDeniedHandler {
  @Autowired
  LoggingService loggingService;

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      AccessDeniedException e) throws IOException, ServletException {

    ErrorDetail response = new ErrorDetail(ErrorCodes.API_ERROR_001_01.getMessage(),
    		MDC.get(Messages.REQUEST_HEADER), ErrorCodes.API_ERROR_001_01.name());
    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    OutputStream out = httpServletResponse.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, response);
    out.flush();
    String responseMessage = this.loggingService.logResponse(httpServletRequest, httpServletResponse, response);
    log.error(responseMessage);
  }

}

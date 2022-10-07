package ec.msplatform.logging;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import ec.msplatform.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

  @Autowired
  LoggingService loggingService;

  @Override
  public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {

    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {

    if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
      String responseMessage = null;
      if (log.isInfoEnabled() || log.isDebugEnabled()) {
        responseMessage = this.loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
            ((ServletServerHttpResponse) response).getServletResponse(), body);
        if (null != responseMessage) {
          log.info(responseMessage);
        }
      } else if (log.isErrorEnabled()) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        HttpStatus status = HttpStatus.valueOf(httpServletResponse.getStatus());
        responseMessage = this.loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
            ((ServletServerHttpResponse) response).getServletResponse(), body);
        if (!status.is2xxSuccessful()) {
          log.error(responseMessage);
        }
      }

    }

    return body;
  }
}

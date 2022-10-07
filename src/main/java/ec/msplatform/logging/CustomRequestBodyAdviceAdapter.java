package ec.msplatform.logging;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import ec.msplatform.common.utilites.JsonUtils;
import ec.msplatform.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {
  @Autowired
  LoggingService loggingService;

  @Autowired
  HttpServletRequest httpServletRequest;

  @Autowired
  JsonUtils jsonUtils;

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    return true;
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    String requestMessage = this.loggingService.logRequest(this.httpServletRequest, body);
    log.info(requestMessage);
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}

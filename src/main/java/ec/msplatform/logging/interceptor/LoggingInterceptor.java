package ec.msplatform.logging.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import ec.msplatform.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
  @Autowired
  LoggingService loggingService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    if (HttpMethod.GET.toString().equals(request.getMethod())
        || HttpMethod.DELETE.toString().equals(request.getMethod())) {
      String requestMessage = this.loggingService.logRequest(request, null);
      log.info(requestMessage);
    }
    return true;
  }

}

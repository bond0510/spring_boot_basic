package ec.msplatform.logging.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ec.msplatform.common.constants.Messages;
import ec.msplatform.common.utilites.JsonUtils;
import ec.msplatform.logging.mask.MaskUtils;
import ec.msplatform.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoggingServiceImpl implements LoggingService {

  @Value("${logging.request.header.parameters}")
  private ArrayList<String> headerParamters = new ArrayList<>();

  @Override
  public String logRequest(HttpServletRequest httpServletRequest, Object body) {

    StringBuilder stringBuilder = new StringBuilder();
    String parameters = buildParametersMap(httpServletRequest);
    String headers = buildHeadersMap(httpServletRequest);
    String consumer = MDC.get(Messages.CONSUMER_ID);

    stringBuilder.append("REQUEST MESSAGE :> ");
    stringBuilder.append("{").append("\"" + "consumer").append("\"").append(":").append("\"").append(consumer)
        .append("\"").append(",").append("\"" + "method").append("\"").append(":").append("\"")
        .append(httpServletRequest.getMethod()).append("\"").append(",");
    stringBuilder.append("\"").append("path").append("\"").append(":").append("\"")
        .append(httpServletRequest.getRequestURI()).append("\"").append(",");
    if (null != headers && !headers.isEmpty()) {
      stringBuilder.append("\"").append("headers").append("\"").append(":").append(headers).append(",");
    }
    if (!parameters.isEmpty()) {
      stringBuilder.append("\"").append("parameters").append("\"").append(":").append(parameters).append(",");
    }
    if (body != null) {
      stringBuilder.append("\"").append("body").append("\"").append(":").append(JsonUtils.getAsJson(body));
    }
    String message = StringUtils.removeEnd(stringBuilder.toString(), ",");
    return message + "}";
  }

  @Override
  public String logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      Object body) {

    String message = null;
    if (!httpServletRequest.getRequestURI().contains("/actuator/")) {
      StringBuilder stringBuilder = new StringBuilder();
      String headers = buildHeadersMap(httpServletResponse);
      String consumer = MDC.get(Messages.CONSUMER_ID);

      stringBuilder.append("RESPONSE MESSAGE :> ");
      stringBuilder.append("{").append("\"" + "consumer").append("\"").append(":").append("\"").append(consumer)
          .append("\"").append(",").append("\"" + "method").append("\"").append(":").append("\"")
          .append(httpServletRequest.getMethod()).append("\"").append(",");
      stringBuilder.append("\"").append("path").append("\"").append(":").append("\"")
          .append(httpServletRequest.getRequestURI()).append("\"").append(",");
      stringBuilder.append("\"").append("status").append("\"").append(":").append("\"")
          .append(httpServletResponse.getStatus()).append("\"").append(",");
      if (!headers.isEmpty()) {
        stringBuilder.append("\"").append("responseHeaders").append("\"").append(":").append(headers).append(",");
      }
      if (body != null) {
        stringBuilder.append("\"").append("body").append("\"").append(":").append(JsonUtils.getAsJson(body));
      }
      message = StringUtils.removeEnd(stringBuilder.toString(), ",");
    }
    if (message != null) {
      return message + "}";
    } else {
      return message;
    }
  }

  private String buildParametersMap(HttpServletRequest httpServletRequest) {

    StringBuilder parameters = new StringBuilder("{");
    Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String key = parameterNames.nextElement();
      String value = httpServletRequest.getParameter(key);
      if (null != value && parameterNames.hasMoreElements()) {
        parameters.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"")
            .append(",");
      } else if (null != value && !parameterNames.hasMoreElements()) {
        parameters.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"");
      }

    }
    parameters.append("}");
    return parameters.toString();
  }

  private String buildHeadersMap(HttpServletRequest request) {

    Enumeration headerNames = request.getHeaderNames();
    StringBuilder headers = new StringBuilder("{");
    while (headerNames.hasMoreElements()) {
      String value = null;
      String key = (String) headerNames.nextElement();

      for (String param : this.headerParamters) {
        if (param.equalsIgnoreCase(key)) {
          value = request.getHeader(key);
          value = MaskUtils.mask(value, Messages.DIGITS_COUNT, Messages.DIGITS_COUNT);
          if (null != value && headerNames.hasMoreElements()) {
            headers.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"")
                .append(",");
          } else if (null != value && !headerNames.hasMoreElements()) {
            headers.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"");
          }
        }
      }
    }
    headers.append("}");

    return headers.toString();
  }

  private String buildHeadersMap(HttpServletResponse response) {

    StringBuilder headers = new StringBuilder("{");
    Collection<String> headerNames = response.getHeaderNames();
    if (null != headerNames && !headerNames.isEmpty()) {
      headerNames.forEach((header) -> {
        headers.append("\"").append(header).append("\"").append(":").append("\"").append(response.getHeader(header))
            .append("\"").append(",");
      });
    }
    String headerString = StringUtils.removeEnd(headers.toString(), ",");
    return headerString + "}";
  }
}

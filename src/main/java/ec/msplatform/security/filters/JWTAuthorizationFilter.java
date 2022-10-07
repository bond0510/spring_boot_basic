package ec.msplatform.security.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ec.msplatform.common.constants.ErrorCodes;
import ec.msplatform.common.constants.Messages;
import ec.msplatform.common.dto.ErrorDetail;
import ec.msplatform.common.dto.Payload;
import ec.msplatform.common.exceptions.AuthenticationException;
import ec.msplatform.common.exceptions.TokenInfoException;
import ec.msplatform.common.utilites.Utils;
import ec.msplatform.logging.service.LoggingService;
import ec.msplatform.security.config.AuthorizationConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

  private static final String HEADER = "Authorization";

  private static final String PREFIX = "Bearer ";

  private static final int TWO = 2;

  private static final int THREE = 3;

  private static final int FOUR = 4;

  private static final int TWELVE = 12;

  @Autowired
  private AuthorizationConfig authorizationConfig;

  @Autowired
  LoggingService loggingService;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Gson gson = new GsonBuilder().create();
    try {
      if (null == SecurityContextHolder.getContext().getAuthentication()) {
        log.debug("Inside JWTAuthorizationFilter: doFilterInternal ");
        final String correlationId = getCorrelationId(request);
        MDC.put(Messages.REQUEST_HEADER, correlationId);
        try {
          if (checkJWTToken(request)) {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
            Optional<Payload> payload = extractTokenPayload(jwtToken);
            if (!payload.isPresent()) {
              log.error("Payload extracted from token is null or empty");
              SecurityContextHolder.clearContext();
              throw new AuthenticationException(ErrorCodes.API_ERROR_001_01.name());
            }
            setUpSpringAuthorization(payload.get());
          } else {
            SecurityContextHolder.clearContext();
            throw new AuthenticationException(ErrorCodes.API_ERROR_001_06.name());
          }
        } catch (Exception exception) {
          SecurityContextHolder.clearContext();
          log.error("Execption occured while authorizing token ", exception);
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          ErrorDetail errorDetail = null;
          if (exception instanceof AuthenticationException) {
            AuthenticationException authenticationException = (AuthenticationException) exception;
            errorDetail = new ErrorDetail(ErrorCodes.valueOf(authenticationException.getMessage()).getMessage(),
                MDC.get(Messages.REQUEST_HEADER), ErrorCodes.valueOf(authenticationException.getMessage()).name());
          } else {
            errorDetail = new ErrorDetail(ErrorCodes.API_ERROR_001_01.getMessage(), MDC.get(Messages.REQUEST_HEADER),
                ErrorCodes.API_ERROR_001_01.name());
          }
          String errorMessage = gson.toJson(errorDetail);
          response.setContentType("application/json");
          PrintWriter writer = response.getWriter();
          writer.write(errorMessage);
          writer.flush();
          String responseMessage = this.loggingService.logResponse(request, response, errorMessage);
          log.error(responseMessage);
          return;
        }
      }
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(Messages.REQUEST_HEADER);
      SecurityContextHolder.clearContext();
    }
  }

  private String getCorrelationId(HttpServletRequest request) {

    final String token;
    if (StringUtils.hasText(request.getHeader(Messages.REQUEST_HEADER))) {
      token = request.getHeader(Messages.REQUEST_HEADER);
    } else {
      token = Utils.getUUID();
    }
    return token;
  }

  private boolean checkJWTToken(HttpServletRequest request) {

    String authenticationHeader = request.getHeader(HEADER);
    return (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX)) ? false : true;
  }

  public Optional<Payload> extractTokenPayload(String jwtToken) {

    try {
      Gson gson = new GsonBuilder().create();
      int numberOfFields = StringUtils.countOccurrencesOf(jwtToken, ".") + 1;
      log.debug("Number of parts found in token: {} (expected 3 or 4 for DK)", numberOfFields);
      String[] jwtParts = jwtToken.split("\\.");
      String jwtEncodedPayload = null;
      if (numberOfFields == THREE) {
        jwtEncodedPayload = jwtParts[1];
      } else if (numberOfFields == FOUR) {
        jwtEncodedPayload = jwtParts[TWO];
      }
      if (null == jwtEncodedPayload) {
        throw new TokenInfoException("No info could be extracted from the token");
      }
      Optional<String> jwtDecodedPayloadOptional = getDecodedPayload(jwtEncodedPayload);
      if (!jwtDecodedPayloadOptional.isPresent()) {
        throw new TokenInfoException("No info could be extracted from the token");
      }

      String clientInfo = jwtDecodedPayloadOptional.get();
      log.debug("Token info --> {}", clientInfo);
      Payload payload = gson.fromJson(clientInfo, Payload.class);
      if (null != payload.getPsd2Ssn() && payload.getPsd2Ssn().length() == TWELVE) {
        log.debug("payload ssn has 12 characters, discarding first 2...");
        payload.setPsd2Ssn(payload.getPsd2Ssn().substring(TWO));
        log.debug("payload ssn will be {}", payload.getPsd2Ssn());
      }
      return Optional.of(payload);
    } catch (Exception e) {
      log.error(String.valueOf(e));
      return Optional.empty();
    }
  }

  private Optional<String> getDecodedPayload(String encodedPayload) {

    try {
      return Optional.of(new String(org.apache.tomcat.util.codec.binary.Base64.decodeBase64URLSafe(encodedPayload)));
    } catch (Exception e) {
      try {
        return Optional.of(new String(Base64.getUrlDecoder().decode(encodedPayload.getBytes(StandardCharsets.UTF_8))));
      } catch (Exception ex) {
        log.error("Failed to decode Payload!");
        return Optional.empty();
      }
    }
  }

  private void setUpSpringAuthorization(Payload payload) {

    log.debug("Seting token authorities");
    List<String> authorities = new ArrayList<String>();
    if (this.authorizationConfig.getReadOnlyAccesss().contains(payload.getAud())) {
      authorities.add(Authority.READ_AUTHORITY.name());
    }
    if (this.authorizationConfig.getReadWriteAccess().contains(payload.getAud())) {
      authorities.add(Authority.WRITE_AUTHORITY.name());
    }
    log.info("Token authorities : {}", authorities);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(payload.getSub(), null,
        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    SecurityContextHolder.getContext().setAuthentication(auth);
    final String consumer = payload.getAud();
    MDC.put(Messages.CONSUMER_ID, consumer);
  }

  @Override
  protected boolean shouldNotFilterAsyncDispatch() {

    return true;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    String path = request.getRequestURI();
    return ("/basic/actuator/health/readiness".equals(path)
        || "/basic/actuator/health/liveness".equals(path)
        || path.contains("/basic/actuator/"));
  }

}

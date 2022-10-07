package ec.msplatform.common.utilites;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ec.msplatform.logging.interceptor.LoggingObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public final class JsonUtils {

  private JsonUtils() {

  }

  public static String getAsJson(Object request) {

    try {
      ObjectMapper mapper = new LoggingObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS).registerModule(new JavaTimeModule());
      return mapper.writeValueAsString(request);
    } catch (JsonProcessingException ignored) {
      log.error("Failed to getJson from {}", request.getClass().getSimpleName(), ignored);
      return "";
    }
  }

  public static String getAsPrettyJson(Object request) {

    try {
      ObjectMapper mapper = new LoggingObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS).registerModule(new JavaTimeModule());
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
    } catch (JsonProcessingException ignored) {
      log.error("Failed to getJson from {}", request.getClass().getSimpleName(), ignored);
      return "";
    }
  }

}

package ec.msplatform.common.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

/**
 * Object to be returned as a response for an error in the request process
 *
 */
@Getter
@JsonInclude(Include.NON_NULL)
public class ErrorDetail {

  private String errorMessage;

  private String timestamp;

  private String uuid;

  private String errorCode;

  /**
   * The constructor.
   *
   * @param errorMessage is the message inside the response
   */
  public ErrorDetail(String errorMessage) {

    this.errorMessage = errorMessage;
    this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    this.uuid = UUID.randomUUID().toString();
  }

  /**
   *
   * @param errorMessage the message
   * @param uuid predefined uuid
   */
  public ErrorDetail(String errorMessage, UUID uuid) {

    this.errorMessage = errorMessage;
    this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    this.uuid = uuid.toString();
  }

  /**
   * The constructor.
   *
   * @param errorMessage
   * @param code
   */
  public ErrorDetail(String errorMessage, String code) {

    this.errorMessage = errorMessage;
    this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    this.uuid = UUID.randomUUID().toString();
    this.errorCode = code;
  }

  public ErrorDetail(String errorMessage, String coorelationId, String code) {

    this.errorMessage = errorMessage;
    this.timestamp = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    this.uuid = coorelationId;
    this.errorCode = code;
  }

  /**
   * The constructor.
   */
  public ErrorDetail() {

  }
}

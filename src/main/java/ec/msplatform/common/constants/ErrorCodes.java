package ec.msplatform.common.constants;

/**
 * ErrorCode for Exceptions
 *
 */
public enum ErrorCodes {
  /* Generic Errors API_ERROR_001_01 to API_ERROR_001_10 */
  /**
   * AUTHENTICATION_ERROR
   */
  API_ERROR_001_01("API_ERROR_001_01", "Supplied Token is Unauthorized for this Operation"),

  /**
   * VALIDATION_ERROR
   */
  API_ERROR_001_02("API_ERROR_001_02", "VALIDATION ERROR"),

  /**
   * INTERNAL_SERVER_ERROR
   */

  API_ERROR_001_03("API_ERROR_001_03", "INTERNAL_SERVER_ERROR"),

  /**
   * AUTHENTICATION_ERROR
   */
  API_ERROR_001_06("API_ERROR_001_06", "Authorization token is mandtory to perform the opertions");

  private final String code;

  private final String message;

  /**
   * @return code
   */
  public String getCode() {

    return this.code;
  }

  /**
   * @return message
   */
  public String getMessage() {

    return this.message;
  }

  /**
   * The constructor.
   */
  private ErrorCodes(final String code,

      final String message) {

    this.message = message;
    this.code = code;
  }
}

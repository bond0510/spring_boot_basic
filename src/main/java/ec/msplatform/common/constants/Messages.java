package ec.msplatform.common.constants;

/**
 * Constants class to manage the service messages
 */
public final class Messages {

  /**
   * Error message for internal service error
   */
  public static final String INTERNAL_SERVICE_ERROR = "Internal service error.";

  /**
   * Message for token error
   */
  public static final String TOKEN_ERROR = "Unspecified error with supplied token.";

  public static final String READ_WRITE_ACCESS = "ReadWrite";

  public static final String READ_ONLY_ACCESS = "ReadOnly";

  public static final String REQUEST_HEADER = "x-correlation-id";

  public static final String AUTHENTICATION_FAILED = "Given consumer is not Authorized to access this API";

  public static final int DIGITS_COUNT = 2;

  public static final char CHAR_DIGTI_ZERO = '0';

  public static final char CHAR_DIGIT_ONE = '1';

  public static final String CONSUMER_ID = "consumer_id";

  /**
   * The constructor.
   */
  private Messages() {

  }
}

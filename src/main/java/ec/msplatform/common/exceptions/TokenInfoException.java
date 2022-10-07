package ec.msplatform.common.exceptions;

import ec.msplatform.common.utilites.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 * Token Info Exception
 *
 */
@Getter
@Setter
public class TokenInfoException extends BaseBusinessException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param message the exception message
   */
  public TokenInfoException(String message) {

    super(message);
    setCorrelationId(Utils.getCorrelationId());
  }

  public TokenInfoException(String message, String correlationId) {

    super(message, correlationId);
  }

}

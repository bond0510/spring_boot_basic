package ec.msplatform.commons;

import lombok.extern.slf4j.Slf4j;

/**
 * Resources class for tests
 */
@Slf4j
public final class Resources {

  /**
   * Netbank AUTH_TOKEN
   */
  public static final String AUTH_TOKEN = "Bearer eyJraWQiOiItODA3MzUwMTU1IiwieDV0IjoiaFJpMGdsQnJtX09QTVBsYmFwaDhYZjJjRDQwIiwiYWxnIjoiUlMyNTYifQ.eyJqdGkiOiJiNGJlYzEzNy1jMzRmLTQyNmYtOGMyOC00OGNiNGI2ZjUxYWEiLCJkZWxlZ2F0aW9uSWQiOiJkZWZjMDIwZC1iNGY2LTQzMzQtYWViMy1iOWU3YmMzMTY5MmIiLCJleHAiOjE1Njc2ODc1MzAsIm5iZiI6MTU2NzY4NzIzMCwic2NvcGUiOiIiLCJpc3MiOiJodHRwczovL2FwaXN0YWdlLmVudGVyY2FyZC5jb20vfiIsInN1YiI6InVzZXJfbWFuYWdlbWVudF9hZG1pbiIsImF1ZCI6Ijk4YTU0ODFiLTQyMjctNGI0ZS05ZWVhLTE3ZDkzZjdlMTBhNSIsImlhdCI6MTU2NzY4NzIzMCwicHVycG9zZSI6ImFjY2Vzc190b2tlbiJ9.OuoEoyJ6Dlj8A8XyGudYhetgfsn5QpU5Io1veSrdwGlgJf98ZGZAQVy_R1O0IF8EyDyFmok3iUXU-oxgV86ynT9atKyKGV-jMgViHb6Ot3sJ4azH25eoFYPc4IiGTJMdgj-QWL3lsTYLhDutsJsJi7Syb3Rwj9g1-ss2iUIBv6YqWk8TRWkASPVJ803QmUC6K83-toyw9XbbcQ71FmiNkHyVLINJTteIwRICh1WnGVK957ep7UKBKMccVuFADXV1a4J7n_33OTlnvZ9ADCkksS98dNG-Z2X5IZfUU1EiJe3gAJBrSNFJllDyuNGA9TxJHYYG_a47SANr2eHEEiYdrw ";

  /**
   * Creates a header of the test in the Logs
   *
   * @param name the name of the test
   */
  public static void testHeader(String name) {

    log.info("------------------------------------------------------------------------------");
    log.info("--- TEST: {}", name);
    log.info("------------------------------------------------------------------------------");
  }

  /**
   * Creates a success message for the test
   */
  public static void testSuccess() {

    log.info("------------------------------------------------------------------------------");
    log.info("TEST PASSED------------------------------------------------------------------");
    log.info("------------------------------------------------------------------------------\n\n\n");
  }

}

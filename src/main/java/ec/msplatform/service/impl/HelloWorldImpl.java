package ec.msplatform.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import ec.msplatform.common.utilites.Utils;
import ec.msplatform.service.api.HelloWorldApi;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nsunilsa
 *
 */

@Slf4j
@Controller
public class HelloWorldImpl implements HelloWorldApi {

  /*
   * Sample HELLO WORLD API just to check the template
   */
  @Override
  public ResponseEntity<Object> getHelloWorld(String authHeader) {

    String correlationId = Utils.getCorrelationId();
    ResponseEntity<Object> response = ResponseEntity.ok().body("Hello world");

    return response;
  }

}

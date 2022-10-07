package ec.msplatform.service.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nsunilsa
 *
 */

@RestController
@RequestMapping(value = "/api")
public interface HelloWorldApi {

  /*
   * Sample HELLO WORLD API just to check the template
   */
  @GetMapping(value = "/helloworld")
  ResponseEntity<Object> getHelloWorld(@RequestHeader(value = "authorization") String authHeader);

}

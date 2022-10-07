package ec.msplatform.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ec.msplatform.Application;
import ec.msplatform.commons.Resources;
import ec.msplatform.security.filters.JWTAuthorizationFilter;

/**
 * @author nsunilsa
 *
 */

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest()
public class HelloWorldApiTest {

  private MockMvc mvc;

  @Autowired
  private JWTAuthorizationFilter jwtAuthorizationFilter;

  @Autowired
  WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() {

    /*
     * mvc = MockMvcBuilders.standaloneSetup(customerEmailApi).addFilters( jwtAuthorizationFilter)
     * .setControllerAdvice(restExceptionHandler).build();
     */
    this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilters(this.jwtAuthorizationFilter)
        .apply(springSecurity()).build();
  }

  @Test
  void getDemoTest_Without_Token() throws Exception {

    Resources.testHeader(Thread.currentThread().getStackTrace()[1].getMethodName());

    // when
    this.mvc.perform(get("/api/helloworld").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());

    Resources.testSuccess();
  }

  @Test
  void getDemoTest_With_valid_Token() throws Exception {

    Resources.testHeader(Thread.currentThread().getStackTrace()[1].getMethodName());
    // when
    MockHttpServletResponse response1 = this.mvc
        .perform(
            get("/api/helloworld").header("Authorization", Resources.AUTH_TOKEN).accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();
    // then
    assertThat(response1).isNotNull();
    Resources.testSuccess();
  }

  @Test
  void getDemoTest_With_Authorized_Token() throws Exception {

    // when
    final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    logger.setLevel(Level.ERROR);
    MockHttpServletResponse response1 = this.mvc
        .perform(get("/api/helloworld").header("Authorization", Resources.AUTH_TOKEN)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();
    // then
    assertThat(response1).isNotNull();
  }

}

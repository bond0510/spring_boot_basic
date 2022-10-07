package ec.msplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ec.msplatform.security.filters.Authority;
import ec.msplatform.security.filters.JWTAuthorizationFilter;
import ec.msplatform.security.filters.RESTAccessDeniedHandler;

/**
 * @author nsunilsa
 *
 */
@SpringBootApplication
public class Application {

  /**
   * @param args arguments
   */
  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }

  @EnableWebSecurity
  @Configuration
  @EnableGlobalMethodSecurity(prePostEnabled = true)
  class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http = http.httpBasic(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);
      // Set session management to stateless
      http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
      http.csrf().disable().addFilterAt(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
          .authorizeRequests().antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
          .antMatchers(HttpMethod.GET, "/api/helloworld/**").hasAuthority(Authority.READ_AUTHORITY.name())
          .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(Authority.WRITE_AUTHORITY.name())
          .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority(Authority.WRITE_AUTHORITY.name())
          .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(Authority.WRITE_AUTHORITY.name()).anyRequest()
          .authenticated().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }
  }

  @Bean
  public JWTAuthorizationFilter jwtAuthorizationFilter() {

    JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter();
    return jwtAuthorizationFilter;
  }

  @Bean
  RESTAccessDeniedHandler accessDeniedHandler() {

    return new RESTAccessDeniedHandler();
  }

}

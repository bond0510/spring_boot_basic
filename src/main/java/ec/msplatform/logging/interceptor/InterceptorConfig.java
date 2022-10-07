package ec.msplatform.logging.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	LoggingInterceptor loggingInterceptor;

	@Override
	 public void addInterceptors(InterceptorRegistry registry) {
	  // this interceptor will be applied to all URLs
	  registry.addInterceptor(loggingInterceptor);
	 }

}

package ec.msplatform.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "authorized")
@PropertySource({ "classpath:/config/application-${spring.profiles.active:local}.properties" })
@Data
public class AuthorizationConfig {
	@Value("#{'${authorized.readonlyaccess.channels}'.split(',')}")
	private List<String> readOnlyAccesss;

	@Value("#{'${authorized.readwriteaccess.channels}'.split(',')}")
	private List<String> readWriteAccess;
}

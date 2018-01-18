package org.springframework.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
@Configuration
public class PojoClientConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    PojoClient client(RestTemplate restTemplate,
                          @Value("${pojo-service.host:http://localhost:8082}") String uri) {
        return new PojoClient(restTemplate, uri);
    }

}

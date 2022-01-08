package xyz.wolkarkadiusz.githubservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${github-service.token}") String token){
        if(token != null)
            return builder.defaultHeader("Authorization", "token " + token).build();
        else
            return builder.build();
    }
}

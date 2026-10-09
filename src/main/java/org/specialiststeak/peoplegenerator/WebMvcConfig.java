package org.specialiststeak.peoplegenerator;

import com.moesif.servlet.MoesifConfiguration;
import com.moesif.servlet.MoesifFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.Filter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    @ConditionalOnProperty(name = "moesif.application-id")
    public Filter moesifFilter(@Value("${moesif.application-id}") String applicationId) {

        MoesifConfiguration config = new MoesifConfiguration() {
            // optionally, add implementation of the configuration
            // see readme for a list of configuration options.
        };

        MoesifFilter moesifFilter = new MoesifFilter(applicationId, config, true);

        // optionally set true to log response and request body
        moesifFilter.setLogBody(true);

        return moesifFilter;
    }

}
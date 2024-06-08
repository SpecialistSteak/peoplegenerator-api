package org.specialiststeak.peoplegenerator;

import com.moesif.servlet.MoesifConfiguration;
import com.moesif.servlet.MoesifFilter;
import org.springframework.context.annotation.Bean;
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

    private static final String applicationId = "eyJhcHAiOiI0OTM6OTUwIiwidmVyIjoiMi4xIiwib3JnIjoiODY6NTcwIiwiaWF0IjoxNzE3MjAwMDAwfQ.4g697OrghcQH9NHsh6Yv2iiuZf8-dntQVr_kBPAjjbM";

    @Bean
    public Filter moesifFilter() {

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
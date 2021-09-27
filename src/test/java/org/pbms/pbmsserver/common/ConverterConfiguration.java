package org.pbms.pbmsserver.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfiguration implements WebMvcConfigurer {

    @Bean
    public TEXT_HTML_Converter converter() {
        return new TEXT_HTML_Converter();
    }

}
package org.barrikeit.chess.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.barrikeit.chess.core.util.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jsonConverter());
  }

  @Bean
  public HttpMessageConverter<Object> jsonConverter() {
    ObjectMapper objectMapper =
        Jackson2ObjectMapperBuilder.json()
            .serializerByType(String.class, new StringSerializer())
            .build();
    return new MappingJackson2HttpMessageConverter(objectMapper);
  }
}

package io.github.masked4j.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.jackson.MaskedModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for Masked4J.
 * <p>
 * Automatically registers the {@link MaskedModule} with the Jackson
 * {@link com.fasterxml.jackson.databind.ObjectMapper}.
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class MaskedAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    /**
     * Creates a {@link MaskedModule} bean.
     *
     * @return the masked module
     */
    public MaskedModule maskedModule() {
        return new MaskedModule();
    }
}

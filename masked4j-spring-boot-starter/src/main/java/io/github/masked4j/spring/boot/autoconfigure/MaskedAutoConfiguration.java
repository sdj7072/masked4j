package io.github.masked4j.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.core.MaskingEngine;
import io.github.masked4j.exception.MaskingFailureStrategy;
import io.github.masked4j.jackson.MaskedModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for Masked4J.
 *
 * <p>Automatically registers the {@link MaskedModule} with the Jackson {@link
 * com.fasterxml.jackson.databind.ObjectMapper} and provides a configured {@link MaskingEngine}.
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(MaskedProperties.class)
@ConditionalOnProperty(
    prefix = "masked4j",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class MaskedAutoConfiguration {

  /**
   * Creates a {@link MaskedModule} bean.
   *
   * @return the masked module
   */
  @Bean
  @ConditionalOnMissingBean
  public MaskedModule maskedModule() {
    return new MaskedModule();
  }

  /**
   * Creates a {@link MaskingEngine} bean with the configured failure strategy.
   *
   * @param properties the Masked4J properties
   * @return the masking engine
   */
  @Bean
  @ConditionalOnMissingBean
  public MaskingEngine maskingEngine(MaskedProperties properties) {
    MaskingFailureStrategy strategy = convertStrategy(properties.getFailureStrategy());
    return new MaskingEngine(strategy);
  }

  private MaskingFailureStrategy convertStrategy(MaskedProperties.FailureStrategy strategy) {
    return switch (strategy) {
      case FAIL_FAST -> MaskingFailureStrategy.FAIL_FAST;
      case IGNORE -> MaskingFailureStrategy.IGNORE;
      case REPLACE_WITH_NULL -> MaskingFailureStrategy.REPLACE_WITH_NULL;
    };
  }
}

package io.github.masked4j.spring.boot.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.masked4j.jackson.MaskedModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class MaskedAutoConfigurationTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  MaskedAutoConfiguration.class,
                  org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class));

  @Test
  void defaultConfiguration() {
    this.contextRunner.run(
        (context) -> {
          assertThat(context).hasSingleBean(MaskedModule.class);
          assertThat(context).hasSingleBean(ObjectMapper.class);
          ObjectMapper mapper = context.getBean(ObjectMapper.class);
          assertThat(mapper.getRegisteredModuleIds())
              .anyMatch(id -> id.toString().equals("io.github.masked4j.jackson.MaskedModule"));
        });
  }

  @Test
  void disabledConfiguration() {
    this.contextRunner
        .withPropertyValues("masked4j.enabled=false")
        .run(
            (context) -> {
              assertThat(context).doesNotHaveBean(MaskedModule.class);
            });
  }
}

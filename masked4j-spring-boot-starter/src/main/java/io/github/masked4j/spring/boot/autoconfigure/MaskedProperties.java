package io.github.masked4j.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuration properties for Masked4J. */
@ConfigurationProperties(prefix = "masked4j")
public class MaskedProperties {

  /** Whether to enable Masked4J auto-configuration. */
  private boolean enabled = true;

  /**
   * Strategy for handling masking failures.
   *
   * <ul>
   *   <li>{@code FAIL_FAST} (default): Throws exception immediately
   *   <li>{@code IGNORE}: Logs warning and returns original value
   *   <li>{@code REPLACE_WITH_NULL}: Sets field to null on failure
   * </ul>
   */
  private FailureStrategy failureStrategy = FailureStrategy.FAIL_FAST;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public FailureStrategy getFailureStrategy() {
    return failureStrategy;
  }

  public void setFailureStrategy(FailureStrategy failureStrategy) {
    this.failureStrategy = failureStrategy;
  }

  /** Strategy for handling masking failures. Maps to {@code MaskingFailureStrategy}. */
  public enum FailureStrategy {
    /** Throws a {@code MaskingException} immediately. Best for development. */
    FAIL_FAST,
    /** Logs a warning and returns the original value. Best for production stability. */
    IGNORE,
    /** Sets the field to null to ensure no sensitive data leaks. */
    REPLACE_WITH_NULL
  }
}

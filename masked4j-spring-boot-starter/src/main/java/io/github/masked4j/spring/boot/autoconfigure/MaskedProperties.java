package io.github.masked4j.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Masked4J.
 */
@ConfigurationProperties(prefix = "masked4j")
public class MaskedProperties {

    /**
     * Whether to enable Masked4J auto-configuration.
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

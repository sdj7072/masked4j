package io.github.masked4j.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.masked4j.Masker;
import java.io.IOException;

/**
 * Jackson serializer that applies masking logic during JSON serialization.
 * <p>
 * This serializer is registered for fields annotated with
 * {@link io.github.masked4j.annotation.Masked}.
 */
public class MaskingSerializer extends JsonSerializer<String> {
    private final Masker masker;

    /**
     * Constructs a new MaskingSerializer with the specified masker.
     *
     * @param masker the masker to use
     */
    public MaskingSerializer(Masker masker) {
        this.masker = masker;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(masker.mask(value));
    }
}

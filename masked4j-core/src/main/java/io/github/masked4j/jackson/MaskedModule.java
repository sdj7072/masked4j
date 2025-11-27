package io.github.masked4j.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

/** Jackson Module that registers the {@link MaskedAnnotationIntrospector}. */
public class MaskedModule extends SimpleModule {
  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);
    context.insertAnnotationIntrospector(new MaskedAnnotationIntrospector());
  }
}

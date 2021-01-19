package com.cloudogu.plantuml;

import sonia.scm.ContextEntry;
import sonia.scm.ExceptionWithContext;

import java.util.Optional;

public class PlantUMLRenderException extends ExceptionWithContext {

  private static final String CODE = "7sSM9vTMp1";

  public PlantUMLRenderException() {
      super(ContextEntry.ContextBuilder.entity("Format", "svg").in("Image", "PlantUML").build(), "error rendering plant uml");
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Optional<String> getUrl() {
    return Optional.of("https://scm-manager.org/plugins/scm-markdown-plantuml-plugin/");
  }
}

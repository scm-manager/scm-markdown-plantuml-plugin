/*
 * Copyright (c) 2020 - present Cloudogu GmbH
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 */

package com.cloudogu.plantuml;

import sonia.scm.ExceptionWithContext;

import java.util.Optional;

import static sonia.scm.ContextEntry.ContextBuilder.entity;

@SuppressWarnings("java:S110") // many parent are kind of normal for exceptions
public class PlantUMLRenderException extends ExceptionWithContext {

  private static final String CODE = "7sSM9vTMp1";
  private static final String URL = "https://scm-manager.org/plugins/scm-markdown-plantuml-plugin/";

  PlantUMLRenderException(Exception cause) {
    super(
      entity("Format", "svg").in("Image", "PlantUML").build(),
      "error rendering plant uml",
      cause
    );
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public Optional<String> getUrl() {
    return Optional.of(URL);
  }
}

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

import sonia.scm.BadRequestException;

import static sonia.scm.ContextEntry.ContextBuilder.entity;

@SuppressWarnings("java:S110") // many parent are kind of normal for exceptions
public class PlantUMLDecodeException extends BadRequestException {

  private static final String CODE = "6ISMX03n61";

  PlantUMLDecodeException(Exception cause) {
    super(
      entity("Format", "svg").in("Image", "PlantUML").build(),
      "Failed to decode plantuml",
      cause
    );
  }

  @Override
  public String getCode() {
    return CODE;
  }
}

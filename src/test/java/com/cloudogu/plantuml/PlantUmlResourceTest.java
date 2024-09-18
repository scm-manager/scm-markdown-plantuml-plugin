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

import net.sourceforge.plantuml.SourceStringReader;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sonia.scm.web.RestDispatcher;

import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;

class PlantUmlResourceTest {

  private RestDispatcher dispatcher;

  @BeforeEach
  void setUpEnvironment() {
    dispatcher = new RestDispatcher();
  }

  @Nested
  class Default {

    private final PlantUmlResource resource = new PlantUmlResource();

    @BeforeEach
    void setUpEnvironment() {
      dispatcher = new RestDispatcher();
      dispatcher.addSingletonResource(resource);
    }

    @Test
    void shouldReturnSvg() throws URISyntaxException, UnsupportedEncodingException {
      MockHttpResponse response = request("SyfFKj2rKt3CoKnELR1Io4ZDoSa70000");
      assertThat(response.getContentAsString()).contains("svg");
      assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
      assertThat(response.getOutputHeaders().getFirst("Content-Type")).hasToString("image/svg+xml");
      assertThat(response.getOutputHeaders().getFirst("Cache-Control")).hasToString("public, max-age=31536000");
    }

    @Test
    void shouldReturnBadRequestForInvalidEncodedPlantUML() throws URISyntaxException {
      MockHttpResponse response = request("notValidEncoded");
      assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

  }

  @Nested
  class RenderException {

    private final PlantUmlResource resource = new ThrowingPlantUmlResource();

    @BeforeEach
    void setUpEnvironment() {
      dispatcher = new RestDispatcher();
      dispatcher.addSingletonResource(resource);
    }

    @Test
    void shouldReturnInternalServerError() throws URISyntaxException {
      MockHttpResponse response = request("gwYi0W00");
      assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

  }

  private MockHttpResponse request(String plantUml) throws URISyntaxException {
    MockHttpRequest request = MockHttpRequest.get("/v2/plantuml/svg/" + plantUml);
    MockHttpResponse response = new MockHttpResponse();
    dispatcher.invoke(request, response);
    return response;
  }

  class ThrowingPlantUmlResource extends PlantUmlResource {

    @Override
    protected SourceStringReader createSourceStringReader(String plantUml) {
      throw new IllegalStateException("no access");
    }
  }

}

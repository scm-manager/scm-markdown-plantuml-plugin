/*
 * MIT License
 *
 * Copyright (c) 2020-present Cloudogu GmbH and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.cloudogu.plantuml;

import net.sourceforge.plantuml.SourceStringReader;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sonia.scm.web.RestDispatcher;

import javax.servlet.http.HttpServletResponse;
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

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

import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.Test;
import sonia.scm.web.RestDispatcher;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;

class PlantUmlResourceTest {

  @Test
  void shouldReturnSvg() throws URISyntaxException, UnsupportedEncodingException {
    PlantUmlResource resource = new PlantUmlResource();
    RestDispatcher restDispatcher = new RestDispatcher();
    restDispatcher.addSingletonResource(resource);
    MockHttpRequest mockHttpRequest = MockHttpRequest.get("/v2/plantuml/svg/SyfFKj2rKt3CoKnELR1Io4ZDoSa70000");
    MockHttpResponse mockHttpResponse = new MockHttpResponse();
    restDispatcher.invoke(mockHttpRequest, mockHttpResponse);
    assertThat(mockHttpResponse.getContentAsString()).contains("svg");
    assertThat(mockHttpResponse.getStatus()).isEqualTo(200);
    assertThat(mockHttpResponse.getOutputHeaders().getFirst("Content-Type")).hasToString("image/svg+xml");
    assertThat(mockHttpResponse.getOutputHeaders().getFirst("Cache-Control")).hasToString("public, max-age=31536000");
  }

}

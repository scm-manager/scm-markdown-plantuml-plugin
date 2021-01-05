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

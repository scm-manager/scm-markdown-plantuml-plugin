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

import com.google.common.annotations.VisibleForTesting;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.code.TranscoderSmart;
import sonia.scm.security.AllowAnonymousAccess;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.Date;

@AllowAnonymousAccess
@Path("v2/plantuml/")
public class PlantUmlResource {

  @GET
  @Path("svg/{content}")
  @Produces("image/svg+xml")
  public Response createSvg(@PathParam("content") String encodedContent) {
    String content = decode(encodedContent);
    StreamingOutput output = render(content);
    return Response.ok(output)
      .lastModified(new Date())
      .header("Cache-Control", "public, max-age=31536000")
      .build();
  }

  private StreamingOutput render(String plantUml) {
    try {
      SourceStringReader reader = createSourceStringReader(plantUml);
      return outputStream -> reader.generateImage(outputStream, new FileFormatOption(FileFormat.SVG));
    } catch (Exception e) {
      throw new PlantUMLRenderException(e);
    }
  }

  @VisibleForTesting
  protected SourceStringReader createSourceStringReader(String plantUml) {
    return new SourceStringReader(plantUml);
  }

  private String decode(String encodedContent)  {
    TranscoderSmart transcoder = new TranscoderSmart();
    try {
      return transcoder.decode(encodedContent);
    } catch (Exception e) {
      throw new PlantUMLDecodeException(e);
    }
  }

}

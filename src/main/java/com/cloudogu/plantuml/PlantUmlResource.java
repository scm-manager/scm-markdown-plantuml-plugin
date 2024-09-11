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

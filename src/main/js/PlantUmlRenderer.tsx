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

import React, { FC, useEffect, useState } from "react";
import { ErrorNotification, Loading, apiClient } from "@scm-manager/ui-components";
import { encode } from "plantuml-encoder";

type Props = {
  value: string;
  indexLinks: { [key: string]: any };
};

const PlantUmlRenderer: FC<Props> = ({ value, indexLinks }) => {
  const [isLoading, setLoading] = useState(true);
  const [error, setError] = useState<any>(null);
  const [imageDataUrl, setImageDataUrl] = useState<string | undefined>();

  const encodedValue = encode(value);
  const renderUrl = indexLinks.plantUml.href.replace("{content}", encodedValue);

  useEffect(() => {
    apiClient
      .get(renderUrl)
      .then(response => response.text())
      .then(result => setImageDataUrl(`data:image/svg+xml;base64,${btoa(result)}`))
      .catch(e => setError(e))
      .finally(() => setLoading(false));
  }, [renderUrl]);

  if (error) {
    return <ErrorNotification error={error} />;
  }

  if (isLoading) {
    return <Loading />;
  }

  return (
    <figure className="image">
      <img src={imageDataUrl} alt="plantuml" />
    </figure>
  );
};

export default PlantUmlRenderer;

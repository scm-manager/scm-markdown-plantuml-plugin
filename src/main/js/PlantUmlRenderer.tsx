import React, { FC, useEffect, useState } from "react";
import { Loading, urls } from "@scm-manager/ui-components";
import { encode } from "plantuml-encoder";

type Props = {
  value: string;
};

const PlantUmlRenderer: FC<Props> = ({ value }) => {
  const [isLoading, setLoading] = useState(true);

  const encodedValue = encode(value);
  const imageUrl = urls.withContextPath("/api/v2/plantuml/svg/") + encodedValue;

  useEffect(() => {
    const img = new Image();
    img.src = imageUrl;
    img.onload = () => setLoading(false);
  }, [imageUrl]);

  if (isLoading) {
    return <Loading />;
  }

  return (
    <figure className="image">
      <img src={imageUrl} alt="plantuml" />
    </figure>
  );
};

export default PlantUmlRenderer;

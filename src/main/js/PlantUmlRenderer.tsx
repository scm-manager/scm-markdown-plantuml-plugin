import React, { FC, useEffect, useState } from "react";
import { Loading } from "@scm-manager/ui-components";
import { encode } from "plantuml-encoder";

type Props = {
  value: string;
  indexLinks: { [key: string]: any };
};

const PlantUmlRenderer: FC<Props> = ({ value, indexLinks }) => {
  const [isLoading, setLoading] = useState(true);

  const encodedValue = encode(value);
  const imageUrl = indexLinks.plantUml.href.replace("{content}", encodedValue);

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

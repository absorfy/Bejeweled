import Lottie from "lottie-react";

import styles from "./Gem.module.css";
import star from "../../../../lottie/star.json";
import explode from "../../../../lottie/explode.json";
import column from "../../../../lottie/vertical.json";
import row from "../../../../lottie/horizontal.json";
import GemImage from "./GemImage";

const effectMap = {
  explode,
  star,
  column,
  row
};

export default function GemWithEffect({ gem }) {
  const effect = gem.impact !== 'NONE' ? effectMap[gem.impact.toLowerCase()] : null;
  return (
    <div className={styles.gemContainer}>
      {effect && (
        <Lottie
          animationData={effect}
          loop
          autoplay
          style={{
            position: "absolute",
            top: -46.5,
            left: -46.5,
            width: 160,
            height: 160,
            pointerEvents: "none",
            zIndex: 1
          }}
        />
      )}

      <GemImage color={gem.color} />
    </div>
  );
}

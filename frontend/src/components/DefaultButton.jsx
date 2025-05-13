import buttonStyles from "./Button.module.css";
import {motion} from "framer-motion";
import React from "react";


export default function DefaultButton({buttonClickHandler, textValue, type = "button", style, disabled=false}) {
  return (
    <motion.button
      type={type}
      className={buttonStyles.defaultButton}
      onClick={buttonClickHandler}
      whileHover={{ scale: 1.05 }}
      whileTap={{ scale: 0.95 }}
      transition={{ type: "spring", stiffness: 300 }}
      style={style}
      disabled={disabled}
    >
      {textValue}
    </motion.button>
  )
}

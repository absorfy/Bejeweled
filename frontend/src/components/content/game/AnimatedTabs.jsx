import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

export default function AnimatedTabs({ tabs }) {
  const [activeTab, setActiveTab] = useState(0);

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "center", marginBottom: "1.5rem" }}>
        <div
          style={{
            display: "flex",
            gap: "1.5rem",
            backgroundColor: "#3c3c3c",
            borderRadius: "12px",
            padding: "0.5rem 1rem",
            position: "relative",
          }}
        >
          {tabs.map((tab, i) => (
            <div
              key={i}
              onClick={() => setActiveTab(i)}
              style={{
                cursor: "pointer",
                fontWeight: activeTab === i ? "bold" : "normal",
                position: "relative",
                padding: "4px 6px",
                color: "white",
              }}
            >
              {tab.label}
              {activeTab === i && (
                <motion.div
                  layoutId="tab-underline"
                  style={{
                    position: "absolute",
                    height: 3,
                    backgroundColor: "#ff5a5f",
                    width: "100%",
                    bottom: -2,
                    left: 0,
                    borderRadius: 3,
                  }}
                />
              )}
            </div>
          ))}
        </div>
      </div>

      <AnimatePresence mode="wait">
        <motion.div
          key={activeTab}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          transition={{ duration: 0.25 }}
        >
          {tabs[activeTab].content}
        </motion.div>
      </AnimatePresence>
    </div>
  );
}

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

export default function AnimatedTabs({ tabs }) {
  const [activeTab, setActiveTab] = useState(0);

  return (
    <div>

      <div style={{ display: "flex", gap: "1.5rem", marginBottom: "1rem", position: "relative" }}>
        {tabs.map((tab, i) => (
          <div
            key={i}
            onClick={() => setActiveTab(i)}
            style={{
              cursor: "pointer",
              fontWeight: activeTab === i ? "bold" : "normal",
              position: "relative",
              padding: "4px 0",
            }}
          >
            {tab.label}
            {activeTab === i && (
              <motion.div
                layoutId="tab-underline"
                style={{
                  position: "absolute",
                  height: 3,
                  backgroundColor: "#ffd700",
                  width: "100%",
                  bottom: 0,
                  left: 0,
                }}
              />
            )}
          </div>
        ))}
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

import GameTabs from "./GameTabs";


export default function GameContent({gameName, gameComponent}) {
  return (
    <div className="d-flex" style={{ width: "100%" }}>
      <div style={{ flex: "0 0 70%" }} className="me-3">
        {gameComponent}
      </div>
      <div style={{ flex: "0 0 28%" }}>
        <GameTabs gameName={gameName} />
      </div>
    </div>
  )
}

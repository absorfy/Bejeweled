import Gem from './Gem.jsx';
import LockTile from "./LockTile";
import AirTile from "./AirTile";

export default function TileContent({tile, index, handleDragEnd, setDirection}) {
  switch (tile.tileName) {
    case 'Gem':
      return <Gem gem={tile} handleDragEnd={handleDragEnd} setDirection={setDirection} index={index} />;
    case 'LockTile':
      return <LockTile lockTile={tile} />;
    default:
      return <AirTile/>;
  }
}

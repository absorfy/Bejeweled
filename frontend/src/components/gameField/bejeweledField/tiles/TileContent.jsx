import Gem from './Gem.jsx';
import LockTile from "./LockTile";
import AirTile from "./AirTile";

export default function TileContent({tile, index, handleDragEnd, setDirection, animating}) {
  switch (tile.tileName) {
    case 'Gem':
      return <Gem gem={tile} handleDragEnd={handleDragEnd} setDirection={setDirection} index={index} animating={animating}/>;
    case 'LockTile':
      return <LockTile lockTile={tile} />;
    default:
      return <AirTile/>;
  }
}

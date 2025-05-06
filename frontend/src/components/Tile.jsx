import Gem from './Gem.jsx';
import LockTile from "./LockTile";

export default function Tile({tile, onTileClick}) {
  switch (tile.tileName) {
    case 'Gem':
      return <Gem gem={tile} onGemClick={onTileClick} />;
    case 'LockTile':
      return <LockTile lockTile={tile} />;
    default:
      return <></>;
  }
}

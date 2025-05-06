import styles from './FieldRow.module.css';
import Tile from "./Tile";

function SwitchTile({tile, rowIndex, colIndex, selectedTile, onTileClick}) {
  const isSelected = selectedTile &&
    selectedTile.row === rowIndex &&
    selectedTile.col === colIndex;

  const tileType = (tile.tileName === 'AirTile') ? styles.airTile :
    ((rowIndex + colIndex) % 2 === 0 ? styles.tileType1 : styles.tileType2)

  return (
    <div
      key={"Tile" + colIndex}
      className={`${styles.tile} ${tileType} ${isSelected ? styles.selected : ''}`}
    >
      <Tile tile={tile} onTileClick={() => onTileClick(colIndex)} />
    </div>
  );
}

export default function FieldRow({rowIndex, tiles, selectedTile, onTileClick}) {
  return (
    <div className={styles.fieldRow}>
      {tiles.map((tile, colIndex) =>
        <SwitchTile
          key={"Tile" + colIndex}
          tile={tile}
          rowIndex={rowIndex}
          colIndex={colIndex}
          selectedTile={selectedTile}
          onTileClick={onTileClick}
        />
      )}
    </div>
  );
}

import {useEffect, useState} from "react";
import gsAxios from "../../api";
import bejeweledStyles from './BejeweledField.module.css';
import SwitchTile from "./SwitchTile";
import Tile from "./Tile";

const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));
export default function BejeweledField() {
  const [currentField, setCurrentField] = useState(null);
  const [fieldTiles, setFieldTiles] = useState(null);
  const [loading, setLoading] = useState(true);
  const [direction, setDirection] = useState(null);
  const [animating, setAnimating] = useState(false);


  useEffect(() => {
    gsAxios.get('/bejeweled/field')
      .then(res => {
        setCurrentField(res.data);
        setFieldTiles(res.data.tiles.flat().map(tile => ({
          ...tile,
          isNew: (!fieldTiles?.find(t => t.id === tile.id)?.isNew ?? true) ?? true
        })));
      })
      .catch(error => {
        console.error(error);
      })
      .finally(() => {
        setLoading(false);
      })
  }, []);

  if (loading) {
    return <div>Loading field..</div>;
  }

  if (!currentField || !currentField.tiles) {
    return <div>ERROR: loading field</div>;
  }


  const animateBreaking = async (frames) => {
    setAnimating(true)
    for(const frame of frames) {
      await sleep(700);
      setCurrentField(frame);
      setFieldTiles(frame.tiles.flat().map(tile => ({
        ...tile,
        isNew: (!fieldTiles?.find(t => t.id === tile.id)?.isNew ?? true) ?? true
      })));
    }
    setAnimating(false)
  }


  function handleDragEnd(event, info, index) {
    if(animating) return;

    const offset = info.offset;
    const threshold = 35;

    const row = Math.floor(index / 8);
    const col = index % 8;

    if (direction === 'x') {
      if (offset.x > threshold) {
        swapFromTo(row, col, row, col + 1)
      } else if (offset.x < -threshold) {
        swapFromTo(row, col, row, col - 1)
      }
    } else if (direction === 'y') {
      if (offset.y > threshold) {
        swapFromTo(row, col, row + 1, col)
      } else if (offset.y < -threshold) {
        swapFromTo(row, col, row - 1, col)
      }
    }
  }

  function swapFromTo(row1, col1, row2, col2) {
    if(row2 < 0 || row2 > 7 || col2 < 0 || col2 > 7) return;

    const index1 = row1 * 8 + col1;
    const index2 = row2 * 8 + col2;

    if (index1 >= 0 && index1 < fieldTiles.length && index2 >= 0 && index2 < fieldTiles.length) {
      if(fieldTiles[index2].tileName !== 'Gem') return;

      const newOrder = [...fieldTiles];
      [newOrder[index1], newOrder[index2]] = [newOrder[index2], newOrder[index1]];
      setFieldTiles(newOrder);

      gsAxios.post('/bejeweled/swap', {
        row1: row1,
        col1: col1,
        row2: row2,
        col2: col2
      }).then(res => {
        animateBreaking(res.data)
      }).catch(err => {
        console.error(err)
      })
    }
  }


  return (
    <ul className={bejeweledStyles.board}>
      {fieldTiles.map((tile, index) => (
        <SwitchTile key={tile.id} tile={tile} rowIndex={Math.floor(index / 8)} colIndex={index % 8}/>
      ))}

      {fieldTiles.map((tile, index) => (
        <Tile key={tile.id} tile={tile} handleDragEnd={handleDragEnd} setDirection={setDirection} index={index}/>
      ))}
    </ul>
  );
}

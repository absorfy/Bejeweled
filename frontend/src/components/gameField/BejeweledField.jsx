import {useEffect, useState} from "react";
import gsAxios from "../../api";
import FieldRow from "./FieldRow";
import styles from './BejeweledField.module.css';
import {flushSync} from "react-dom";

const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));
export default function BejeweledField() {
  const [currentField, setCurrentField] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedTile, setSelectedTile] = useState(null);
  const [animating, setAnimating] = useState(false);
  const animationDelay = 700;



  const animateBreaking = async (frames) => {
    setAnimating(true)
    for(const frame of frames) {
      setCurrentField(frame);
      await sleep(animationDelay);
    }
    setAnimating(false)
  }

  const handleTileClick = (row, col) => {
    if(animating) return;

    if(!selectedTile) {
      setSelectedTile({row, col})
      return
    }

    const same = selectedTile.row === row && selectedTile.col === col;
    const adjacent =
      Math.abs(selectedTile.row - row) + Math.abs(selectedTile.col - col) === 1;

    if(same) {
      setSelectedTile(null)
    } else if (adjacent) {
      gsAxios.post('/bejeweled/swap', {
        row1: selectedTile.row,
        col1: selectedTile.col,
        row2: row,
        col2: col
      }).then(res => {
        animateBreaking(res.data)
      }).catch(err => {
        console.error(err)
      }).finally(() => {
        setSelectedTile(null)
      })
    } else {
      setSelectedTile({row, col})
    }
  }


  useEffect(() => {
    gsAxios.get('/bejeweled/field')
      .then(res => {
        setCurrentField(res.data);
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


  return (
    <div className={styles.board}>
      {currentField.tiles.map((tiles, rowIndex) => (
        <FieldRow
          rowIndex={rowIndex}
          tiles={tiles}
          selectedTile={selectedTile}
          key={"Row" + rowIndex}
          onTileClick={(colIndex) => handleTileClick(rowIndex, colIndex) }/>
      ))}
    </div>
  );
}

import {useEffect, useState} from "react";
import gsAxios from "../api";
import FieldRow from "./FieldRow";
import styles from './BejeweledField.module.css';


export default function BejeweledField() {
  const [field, setField] = useState(null); // null замість []
  const [loading, setLoading] = useState(true);
  const [selectedTile, setSelectedTile] = useState(null);
  const animationDelay = 700;


  const animateBreaking = async (currentField) => {
    while (currentField.state === "BREAKING") {
      await new Promise(resolve => setTimeout(resolve, animationDelay))

      await gsAxios.post('/bejeweled/process')
        .then(res => {
          currentField = res.data;
          setField(currentField);
        });

      await new Promise(resolve => setTimeout(resolve, animationDelay))

      await gsAxios.post('/bejeweled/fill')
        .then(res => {
          currentField = res.data;
          setField(currentField);
        });

      await new Promise(resolve => setTimeout(resolve, animationDelay))

      await gsAxios.post('/bejeweled/check')
        .then(res => {
          currentField = res.data;
          setField(currentField);
        });
    }
  };

  const handleTileClick = (row, col) => {
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
        setField(res.data)
        if(res.data.state === "BREAKING") {
          animateBreaking(res.data)
        }
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
        setField(res.data);
        setLoading(false);
      })
      .catch(error => {
        console.error(error);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div>Loading field..</div>;
  }

  if (!field || !field.tiles) {
    return <div>ERROR: loading field</div>;
  }


  return (
    <div className={styles.board}>
      {field.tiles.map((tiles, rowIndex) => (
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

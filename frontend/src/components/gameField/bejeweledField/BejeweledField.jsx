import {useEffect, useState} from "react";
import bejeweledStyles from './BejeweledField.module.css';
import TileBackground from "./tiles/TileBackground";
import TileContent from "./tiles/TileContent";
import {animateBreaking} from "./bejeweledLogic";
import ScoreDisplay from "../display/ScoreDisplay";
import {getHint, startGame, swapGems, testField} from "../../../api/bejeweled.service";
import {usePlayer} from "../../PlayerContext";
import {addScore} from "../../../api/score.service";
import RestartWindow from "../display/RestartWindow";
import DefaultButton from "../../DefaultButton";

const swapAudio = new Audio(`/sounds/swap.ogg`);
const startAudio = new Audio(`/sounds/start.ogg`);

export default function BejeweledField() {
  const [currentField, setCurrentField] = useState({});
  const [fieldTiles, setFieldTiles] = useState(null);
  const [loading, setLoading] = useState(true);
  const [direction, setDirection] = useState(null);
  const [animating, setAnimating] = useState(true);
  const {playerLogin} = usePlayer()
  const [scoreSent, setScoreSent] = useState(false);

  useEffect(() => {
    if (currentField.fieldState === "NO_POSSIBLE_MOVE" && !scoreSent && playerLogin) {
      addScore("bejeweled", playerLogin, currentField.score)
        .then(() => {
        console.log("Score sent successfully.");
        setScoreSent(true);
      })
        .catch(console.error);
    }
  }, [currentField.fieldState, playerLogin])

  useEffect(() => {
    startAudio.currentTime = 0;
    startAudio.play()
    startGame()
      .then(res => {
        setCurrentField(res.data);
        setFieldTiles(res.data.tiles.flat().map(tile => ({
          ...tile,
          isNew: true,
          isStart: true
        })));
      })
      .catch(error => {
        console.error(error);
      })
      .finally(() => {
        setLoading(false);
        setTimeout(() => setAnimating(false), 2000)
      })
  }, []);

  if (loading) {
    return <></>;
  }

  if (!currentField || !currentField.tiles) {
    return <div>ERROR: loading field</div>;
  }

  function handleRestart() {
    startAudio.currentTime = 0;
    startAudio.play()
    setAnimating(true);
    setLoading(true);
    startGame()
      .then(res => {
        setScoreSent(false)
        setCurrentField(res.data);
        setFieldTiles(res.data.tiles.flat().map(tile => ({
          ...tile,
          isNew: true,
          isStart: true,
        })));
      })
      .catch(console.error)
      .finally(() => {
        setLoading(false)
        setTimeout(() => setAnimating(false), 2000)
      });
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

  function handleTestGameOver() {
    testField().then(res => {
      setCurrentField(res.data)
      setFieldTiles(res.data.tiles.flat())
    })
  }

  function swapFromTo(row1, col1, row2, col2, sendToServer = true) {
    if (row2 < 0 || row2 > 7 || col2 < 0 || col2 > 7) return;

    const index1 = row1 * 8 + col1;
    const index2 = row2 * 8 + col2;

    if (
      index1 >= 0 && index1 < fieldTiles.length &&
      index2 >= 0 && index2 < fieldTiles.length
    ) {
      if (fieldTiles[index2].tileName !== 'Gem') return;

      const newOrder = [...fieldTiles];
      [newOrder[index1], newOrder[index2]] = [newOrder[index2], newOrder[index1]];
      setFieldTiles(newOrder);

      swapAudio.play()

      setAnimating(true)
      if (sendToServer) {
        swapGems(row1, col1, row2, col2)
          .then(res => {
            animateBreaking(res.data, setCurrentField, setFieldTiles, fieldTiles)
              .then(() => {
                setAnimating(false)
              });
          })
          .catch(err => {
            console.error(err);
          });
      } else {
        setTimeout(() => {
          [newOrder[index1], newOrder[index2]] = [newOrder[index2], newOrder[index1]];
          setFieldTiles(newOrder);
          setAnimating(false)
        }, 1000);
      }
    }
  }


  function hintHandler() {
    if (currentField.hintCount <= 0) return;

    getHint().then(res => {
      swapFromTo(res.data.row1, res.data.col1, res.data.row2, res.data.col2, false);
      currentField.hintCount--;
    });
  }

  return (
      <div className={bejeweledStyles.bejeweledContent}>
        <div className={bejeweledStyles.gameInfoPanel}>
          <ScoreDisplay score={currentField.score} lastIncrement={currentField.lastIncrementScore} chainCombo={currentField.chainCombo} speedCombo={currentField.speedCombo}/>
          <DefaultButton buttonClickHandler={hintHandler} textValue={currentField.hintCount > 0 ? `Hint ${currentField.hintCount}` : 'No Hints'} disabled={currentField.hintCount <= 0 || animating} />
          <DefaultButton buttonClickHandler={handleRestart} textValue={"New Game"} disabled={animating} />
          <DefaultButton buttonClickHandler={handleTestGameOver} textValue={"Test (Game Over)"} disabled={animating} />
        </div>
        <ul className={bejeweledStyles.board}>
          {fieldTiles.map((tile, index) => (
            <TileBackground
              key={tile.id}
              tile={tile}
              rowIndex={Math.floor(index / 8)}
              colIndex={index % 8}
            />
          ))}

          {fieldTiles.map((tile, index) => (
            <TileContent
              key={tile.id}
              tile={tile}
              handleDragEnd={handleDragEnd}
              setDirection={setDirection}
              index={index}
              animating={animating}
            />
          ))}
        </ul>

        <RestartWindow
          currentField={currentField}
          handleRestart={handleRestart}
        />
      </div>
  );
}

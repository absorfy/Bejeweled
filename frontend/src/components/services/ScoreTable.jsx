import {usePlayer} from "../PlayerContext";
import {useEffect, useState} from "react";
import {fetchScores} from "../../api/score.service";
import useSocket from "../../api/webSocket";
import styles from "./Table.module.css";

export default function ScoreTable({gameName}) {
  const [scores, setScores] = useState([]);
  const {playerLogin} = usePlayer()


  useEffect(() => {
    fetchScores(gameName).then(response => {
      setScores(response.data);
    });
  }, [gameName]);

  useSocket('scores', gameName, (newScore) => {
    (async () => {
      const response = await fetchScores(gameName);
      setScores(response.data);
    })();
  })


  return (
    <div className={styles.tabContainer}>
      <table className="table table-sm" style={{
        boxShadow: "0 0 5px #ffffff",
        marginBottom: 0,
      }}>
        <thead className="thead-dark">
        <tr>
          <th scope="col" style={{width: "10%"}}>#</th>
          <th scope="col" style={{width: "30%"}}>Player</th>
          <th scope="col" style={{width: "30%"}}>Points</th>
          <th scope="col" style={{width: "30%"}}>Date</th>
        </tr>
        </thead>
        <tbody>
        {scores.map((score, index) => (
          <tr key={index}>
            <td>{index + 1}</td>
            <td>{score.playerLogin}</td>
            <td>{score.points}</td>
            <td>{new Date(score.playedOn).toLocaleDateString()}</td>
          </tr>
        ))}
        </tbody>
      </table>

      {playerLogin === null ? (
        <span className={styles.text}>Login to save your points</span>
      ) : (
        <></>
      )}

    </div>
  );
}

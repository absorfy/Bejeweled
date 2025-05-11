import {usePlayer} from "../PlayerContext";
import {useEffect, useState} from "react";
import {fetchScores} from "../../api/score.service";
import useSocket from "../../api/webSocket";


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
    <>
      <table className="table table-sm">
        <thead className="thead-dark">
        <tr>
          <th scope="col">#</th>
          <th scope="col">Player</th>
          <th scope="col">Points</th>
          <th scope="col">Date</th>
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

      {playerLogin === null && (
        <span>Register to save your points</span>
      )}

    </>
  );
}

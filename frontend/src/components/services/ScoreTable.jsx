import {usePlayer} from "../index/PlayerContext";
import {useEffect, useState} from "react";
import {fetchScores} from "../../api/score.service";


export default function ScoreTable({gameName}) {
  const [scores, setScores] = useState([]);
  const {playerLogin} = usePlayer()


  useEffect(() => {
    fetchScores(gameName).then(response => {
      setScores(response.data);
    });
  }, [gameName]);


  return (
    <>
      <table className="table">
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

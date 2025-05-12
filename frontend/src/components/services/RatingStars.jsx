import StarRatings from 'react-star-ratings';
import { usePlayer } from "../PlayerContext";
import {fetchAverageRating, fetchRating, setRating} from "../../api/rating.service";
import {useEffect, useState} from "react";
import useSocket from "../../api/webSocket";
import styles from "./Table.module.css"

export default function RatingStars({ gameName }) {
  const { playerLogin } = usePlayer();
  const [averageRating, setAverageRating] = useState()
  const [playerRating, setPlayerRating] = useState(0);

  useEffect(() => {
    fetchAverageRating(gameName).then(response => {
      setAverageRating(response.data)
    });
  }, [gameName]);

  useEffect(() => {
    fetchRating(gameName, playerLogin).then(response => {
      setPlayerRating(response.data)
    });
  }, [gameName, playerLogin]);

  const handleRatingChange = async (newRating) => {
    if (!playerLogin) {
      alert("Login to rate the game!");
      return;
    }

    try {
      await setRating(gameName, playerLogin, newRating);
      setPlayerRating(newRating)
    } catch (error) {
      console.error("Saving rating error:", error);
      alert("Could not save rating.");
    }
  };

  useSocket('ratings', gameName, (newRating) => {
    (async () => {
      const response = await fetchAverageRating(gameName);
      setAverageRating(response.data)
    })();
  })

  return (
    <div className={styles.tabContainer} style={{paddingBottom: 40}}>
      <h5>Average rating</h5>
      <StarRatings
        rating={averageRating}
        starRatedColor="#ff5a5f"
        numberOfStars={5}
        name="game-rating"
        starDimension="30px"
        starSpacing="5px"
      />
      { playerLogin ? (
        <>
          <h5>Rate the game</h5>
          <StarRatings
            rating={playerRating}
            starRatedColor="#ff5a5f"
            changeRating={handleRatingChange}
            numberOfStars={5}
            name="game-rating"
            starDimension="30px"
            starSpacing="5px"
          />
        </>
      ) : (
        <h5>Login to rate the game</h5>
      )
      }

    </div>
  );
}

import gsAxios from "./index";
import {formatDate} from "./utils";


export const fetchScores = game => gsAxios.get('/score/' + game);
export const addScore = (game, playerLogin, points) => gsAxios.post('/score', {
  game, playerLogin, points, playedOn: formatDate(new Date()),
})

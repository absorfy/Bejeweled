import gsAxios from "./index";
import {formatDate} from "./utils";


export const fetchAverageRating = game => gsAxios.get('/rating/' + game);
export const fetchRating = (game, playerLogin) => gsAxios.get(`/rating/${game}/${playerLogin}`, );
export const setRating = (game, playerLogin, rating) => gsAxios.post('/rating', {
  game, playerLogin, rating, ratedOn: formatDate(new Date()),
})

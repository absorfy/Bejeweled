import gsAxios from "./index";
import {formatDate} from "./utils";


export const fetchComments = game => gsAxios.get('/comment/' + game);
export const addComment = (game, playerLogin, comment) => gsAxios.post('/comment', {
    game, playerLogin, comment, commentedOn: formatDate(new Date()),
})

import gsAxios from "./index";
import {formatDate} from "./utils";


export const fetchComments = game => gsAxios.get('/comment/' + game);
export const addComment = (game, player, comment) => gsAxios.post('/comment', {
    game, player, comment, commentedOn: formatDate(new Date()),
})
import {addComment} from "../api/comment.servise";


export async function handleCommentSubmit(gameName, playerLogin, comment) {
  try {
    await addComment(gameName, playerLogin, comment);
  } catch (error) {
    if (error.response && error.response.status === 404) {
      alert("Player nickname not found. Please check your login.");
    } else {
      alert("An error occurred while posting the comment.");
    }
  }
}

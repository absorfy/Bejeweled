import {fetchComments} from "../../api/comment.servise";
import {usePlayer} from "../index/PlayerContext";
import {useEffect, useState} from "react";
import {handleCommentSubmit} from "./CommentsTableOperations";
import useSocket from "../../api/webSocket";


export default function CommentsTable({gameName}) {
  const [comments, setComments] = useState([]);
  const [comment, setComment] = useState("");
  const {playerLogin} = usePlayer()


  useEffect(() => {
    fetchComments(gameName).then(response => {
      setComments(response.data);
    });
  }, [gameName]);

  useSocket('comments', gameName, (newComment) => {
    setComments(prev => [...prev, newComment])
  })

  async function handleSubmit(e) {
    e.preventDefault();
    await handleCommentSubmit(gameName, playerLogin, comment)
    const response = await fetchComments(gameName);
    setComments(response.data)
    setComment("")
  }

  return (
    <>
      <table className="table">
        <thead className="thead-dark">
        <tr>
          <th scope="col">Player</th>
          <th scope="col">Comment</th>
          <th scope="col">Date</th>
        </tr>
        </thead>
        <tbody>
        {comments.map((comment, index) => (
          <tr key={index}>
            <td>{comment.playerLogin}</td>
            <td>{comment.comment}</td>
            <td>{new Date(comment.commentedOn).toLocaleDateString()}</td>
          </tr>
        ))}
        </tbody>
      </table>

      {playerLogin === null ? (
        <span>Register to comment</span>
        ) : (
        <form
          onSubmit={handleSubmit}
          className="mt-3"
        >
          <div className="mb-3">
            <label htmlFor="comment" className="form-label">Comment</label>
            <textarea className="form-control" value={comment} onChange={e => setComment(e.target.value)} name="comment" rows="3" required></textarea>
          </div>
          <button type="submit" className="btn btn-primary">Post Comment</button>
        </form>
      )}

    </>
  );
}

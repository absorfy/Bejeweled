import AnimatedTabs from "./AnimatedTabs";
import ScoreTable from "../../services/ScoreTable";
import RatingStars from "../../services/RatingStars";
import CommentsTable from "../../services/CommentsTable";

export default function GameTabs({ gameName }) {

  const tabs = [
    {
      label: "Scores",
      content: <ScoreTable gameName={gameName}/>,
    },
    {
      label: "Rating",
      content: <RatingStars gameName={gameName}/>,
    },
    {
      label: "Comments",
      content: <CommentsTable gameName={gameName}/>,
    },
  ];

  return <AnimatedTabs tabs={tabs} />;
}

export const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

export async function animateBreaking(frames, setField, setTiles, setAnimating, oldTiles) {
  setAnimating(true);
  for (const frame of frames) {
    await sleep(700);
    setField(frame);
    setTiles(frame.tiles.flat().map(tile => ({
      ...tile,
      isNew: oldTiles?.find(t => t.id === tile.id)?.isNew !== true,
    })));
  }
  setAnimating(false);
}

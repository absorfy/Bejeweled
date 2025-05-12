
export const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

const hitAudio = new Audio('/sounds/gem_hit.ogg');

export async function animateBreaking(frames, setField, setTiles, oldTiles) {
  for (const [index, frame] of frames.entries()) {
    const breakingAudio = new Audio(`/sounds/combo/combo_${Math.floor(Math.random() * 6)}.ogg`);
    const currentTileIds = frame.tiles.flat().map(tile => tile.id);
    const disappearing = oldTiles?.filter(t => !currentTileIds.includes(t.id)) || [];

    if((index + 2) % 3 === 0 ) {
      await breakingAudio.play();
    }
    if(index !== 0 && (index + 3) % 3 === 0 ) {
      await hitAudio.play();
    }

    if (disappearing.length > 0) {

      const tilesWithDisappearance = [
        ...oldTiles.map(tile =>
          disappearing.find(d => d.id === tile.id)
            ? { ...tile, isDisappearing: true }
            : tile
        )
      ];
      setTiles(tilesWithDisappearance);
      await sleep(300);
    }


    setField(frame);
    setTiles(
      frame.tiles.flat().map((tile, index) => {
        const prevTile = oldTiles?.[index];
        const wasLocked = prevTile?.tileName === 'LockTile';
        const becomeImpact = tile.impact !== 'NONE';

        const isNew = !oldTiles?.some(t => t.id === tile.id);
        const justAppeared = isNew && (wasLocked || becomeImpact);

        return {
          ...tile,
          isNew,
          justAppeared,
        };
      })
    );
    await sleep(700);
    oldTiles = frame.tiles.flat()
  }
}


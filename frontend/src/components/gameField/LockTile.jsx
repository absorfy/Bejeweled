export default function LockTile({lockTile}) {
  return (
    <>
      <span style={{color: lockTile.gem.color}}>{lockTile.needBreakCount}</span>
    </>
  )
}

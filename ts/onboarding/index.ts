export
function* run(readline: () => string) {
  const readEnemy = () => ({name: readline(), dist: parseInt(readline())});
  const enemy1 = readEnemy();
  const enemy2 = readEnemy();
  yield enemy1.dist < enemy2.dist ? enemy1.name : enemy2.name
}

// @ts-ignore
if (typeof readline !== "undefined") while (true) for (const line of run(readline)) console.log(line)

const throwError = (message?: string) => {
  throw new Error(message)
}

class Cell {
  constructor(readonly x: number, readonly y: number, readonly alive: boolean,) {
  }

  static parse = (char: string, x: number, y: number): Cell => new Cell(x, y, char === "O")

  get char(): string {
    return this.alive ? "O" : ".";
  }

  next = (change: boolean) => {
    return new Cell(this.x, this.y, this.alive !== change)
  }
}

class Map {
  constructor(readonly cells: Cell[]) {
  }

  static parse = (lines: string[]): Map => new Map(lines.flatMap((line, y) => Array.from(line).map((c, x) => Cell.parse(c, x, y))))

  next = (worldRule: WorldRule) => {
    return new Map(this.cells.map(cell => worldRule(cell, this.#neightbours(cell))));
  };

  * lines() {
    for (let y = 0; y <= this.cells[this.cells.length - 1].y; y += 1)
      yield this.cells.filter(c => c.y === y).map(c => c.char).join("")
  }

  #neightbours = ({x, y}: Cell) => {
    return this.cells.filter(current => (x - 1 < current.x && current.x < x + 1) && (y - 1 < current.y && current.y < y + 1) && (x !== current.x || y !== current.y));
  }
}

type WorldRule = (cell: Cell, neightbours: Cell[]) => Cell
const parseWorldRule = (revive: string, die: string): WorldRule => (cell, neightbours) => cell.next(
  (cell.alive ? die : revive)[neightbours.filter(c => c.alive).length] === "1"
)

export function* run(readline: () => string): Generator<string> {
  const [h, w, n] = readline().split(' ').map(parseInt);
  const worldRule = parseWorldRule(readline(), readline());
  const initialMap = Map.parse([...Array(h).keys()].map(readline));

  const finalMap = [...Array(n).keys()].reduce(currentMap => currentMap.next(worldRule), initialMap)

  for (const line of finalMap.lines()) yield line
}

// @ts-ignore for testing outside
if (typeof readline !== "undefined") for (const line of run(readline)) console.log(line.toString())
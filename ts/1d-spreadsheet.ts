type Operation = (ops: Operation[]) => number
type Arg = (ops: Operation[]) => number

const cached = (op:Operation):Operation => {
  let value: number | undefined = undefined;
  return ops => value ?? (value = op(ops));
}

const parseArg = (s: string): Arg => s[0] === "$"
  ? (ops) => ops[Number(s.slice(1))](ops)
  : () => Number(s)

const parseOperation = (s: string): Operation => {
  const splitted = s.split(" ");
  const arg = (index: 1 | 2) => parseArg(splitted[index])
  const binaryOp = (binaryOp: (a: number, b: number) => number):Operation => ops => binaryOp(arg(1)(ops), arg(2)(ops))

  return {
    "VALUE": arg(1),
    "ADD": binaryOp((a, b) => a + b),
    "SUB": binaryOp((a, b) => a - b),
    "MULT": binaryOp((a, b) => a * b),
  }[splitted[0]] ?? (() => { throw new Error(`cannot parse ${s}`) })()
}

export function* run(readline: () => string): Generator<number> {
  const operations = [...(Array(parseInt(readline())).keys())]
    .map(readline)
    .map(parseOperation)
    .map(cached)
  for (const operation of operations) yield operation(operations)
}

// @ts-ignore for testing outside
if (typeof readline !== "undefined") for (const line of run(readline)) console.log(line.toString())
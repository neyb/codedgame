const throwError = (err: string) => {
  throw new Error(err)
}

export const createReadline = (lines: string[]) => {
  let index = 0;
  return () => lines[index++] ?? throwError("no more lines to read");
}
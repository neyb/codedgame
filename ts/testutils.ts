export const createReadline = (lines: string | string[]): () => string =>
  Array.isArray(lines)
    ? (() => {
      let index = 0;
      return () => lines[index++];
    })()
    : createReadline(lines.split("\n").slice(1));

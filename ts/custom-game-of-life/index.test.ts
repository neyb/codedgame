import {assertEquals} from "https://deno.land/std/testing/asserts.ts";
import {createReadline} from "../testutils.ts";
import {run} from "./index.ts";

Deno.test('after 0 turns, send input as output', () => {
  let lines = [...run(createReadline([
    '1 1 0',
    "000000000",
    "000000000",
    '.'
  ]))];

  assertEquals(lines, ['.'])
})

Deno.test('with empty revive/die map would stay the same', () => {
  let lines = [...run(createReadline([
    '3 3 1',
    "000000000",
    "000000000",
    'O..',
    '.O.',
    'O.O',
  ]))];

  assertEquals(lines, [
    'O..',
    '.O.',
    'O.O',
  ])
})

Deno.test('sample', () => {
  const lines = [...run(createReadline([
    "3 4 1",
    "001100000",
    "000100000",
    ".OO.",
    "O..O",
    ".OO.",
  ]))]

  assertEquals(lines, [
    ".OO.",
    "O..O",
    ".OO.",
  ])
})
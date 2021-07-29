import {run} from "./1d-spreadsheet.ts";
import {assertEquals} from "https://deno.land/std/testing/asserts.ts";
import {createReadline as commonCreateReadline} from "./testutils.ts";

const createReadline = (lines: string[]) => commonCreateReadline([lines.length.toString(), ...lines])

const testWith = (inputLines: string[], expectedResult:number[]) => {
  assertEquals([...run(createReadline(inputLines))], expectedResult)
}

Deno.test('no value print nothing', () => {
  testWith([],[])
});

Deno.test('1 simple literal value', () => {
  testWith(["VALUE 1 _"],[1])
})

Deno.test('sample test', () => {
  testWith(["VALUE 1 _", "ADD 1 $0"], [1,2])
})
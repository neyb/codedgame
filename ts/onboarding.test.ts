import {createReadline} from "./testutils.ts";
import {assertEquals} from "https://deno.land/std@0.102.0/testing/asserts.ts";
import {run} from "./onboarding.ts";

Deno.test("simple inputs", () => {
  let output = run(createReadline(`
enemy1
1
enemy2
2`));

  assertEquals([...output], ["enemy1"]);
});

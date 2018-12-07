package graph

import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec

class LinkTest :FreeSpec ({
    "from" - {
        "2-1 from 1 is 1-2" {
            val from1 = Link(1, 2).from(2)
            from1.from shouldBe 2
            from1.to shouldBe 1
        }
    }
})
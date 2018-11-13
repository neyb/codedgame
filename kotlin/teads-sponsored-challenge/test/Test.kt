import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.FreeSpec

class Test : FreeSpec({
    "1-2 1-3 1-4 4-5 should have leafs 2, 3 and 5" {
        val graph = Graph(1 to 2, 1 to 3, 1 to 4, 4 to 5)

        graph.leafs().toSet() shouldBe setOf(2, 3, 5)
    }

    "min depth" - {
        "of empty graph should be 0" {
            val graph = Graph()
            graph.minDepth() shouldBe 0
        }

        "of 1<->2 should be 1" {
            val graph = Graph(1 to 2)

            graph.minDepth() shouldBe 1
        }

        "of 1<->2<->3 should be 1" {
            val graph = Graph(1 to 2, 2 to 3)

            graph.minDepth() shouldBe 1
        }

        "of 1<->2<->3<->4 should be 2" {
            val graph = Graph(1 to 2, 2 to 3, 3 to 4)
            graph.minDepth() shouldBe 2
        }

        "of 1<->2<->3, 2<->4 should be 1" {
            val graph = Graph(1 to 2, 2 to 3, 2 to 4)
            graph.minDepth() shouldBe 1
        }

        "of 1-2-3-4-5-6, 3-7, 4,8 should be 3" {
            val graph = Graph(1 to 2,2 to 3,3 to 4,4 to 5,5 to 6, 3 to 7, 4 to 8)
            graph.minDepth() shouldBe 3
        }
    }

    """6<->8 != 14<->10""" {
        Link(14, 10) shouldNotBe Link(6,8)
    }
})
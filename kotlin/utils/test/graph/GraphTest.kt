package graph

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec

class GraphTest : FreeSpec({


    "closests" - {

        val graph3x3 = Graph(
            Link("0.0", "0.1"),
            Link("0.0", "1.0"),
            Link("0.1", "0.2"),
            Link("0.1", "1.1"),
            Link("0.2", "1.2"),
            Link("1.0", "1.1"),
            Link("1.0", "2.0"),
            Link("1.1", "1.2"),
            Link("1.1", "2.1"),
            Link("1.2", "2.2"),
            Link("2.0", "2.1"),
            Link("2.1", "2.2")
        )

        "1 answer" {
            val closests = graph3x3.bfsClosests("0.0") { it == "2.2" }
            closests shouldContainExactlyInAnyOrder setOf("2.2")
        }

        "1 answer (the other is too far)" {
            val closests = graph3x3.bfsClosests("0.0") { it == "2.2" || it == "1.1" }
            closests shouldContainExactlyInAnyOrder setOf("1.1")
        }

        "2 answer (others are too far)" {
            val closests = graph3x3.bfsClosests("0.0") { it.startsWith("2") || it.endsWith("2") }
            closests shouldContainExactlyInAnyOrder setOf("2.0", "0.2")
        }

    }

    "pathto" - {

        "linear graph" - {
            val linearGraph = Graph(Link("1", "2"), Link("2", "3"), Link("3", "4"))

            "1-2-3-4, 1 pathto 2 is 1-2" {
                val actualPath = linearGraph.findPath("1", "2")
                actualPath.shouldContainExactly(Path("1", listOf(Link("1", "2"))))
            }

            "1-2-3-4, 2 pathto 1 is 2-1" {
                val actualPath = linearGraph.findPath("2", "1")
                actualPath.shouldContainExactly(Path("2", listOf(Link("2", "1"))))
            }

            "1-2-3-4, 1 pathto 3 is 1-2-3" {
                val actualPath = linearGraph.findPath("1", "3")
                actualPath.shouldContainExactly(Path("1", listOf(Link("1", "2"), Link("2", "3"))))
            }
        }

        "poundedGraph" - {
            val poundedGraph = Graph(
                Link("A", "C", 1.0),
                Link("A", "B", 3.0),
                Link("C", "B", 7.0),
                Link("B", "D", 5.0),
                Link("C", "D", 2.0),
                Link("B", "E", 1.0),
                Link("E", "D", 7.0)
            )

            "B -> C is B->A->C (weight:3+1=4)" {
                val paths = poundedGraph.findPath("B", "C")
                paths.size shouldBe 1
                val path = paths.first()
                path shouldBe Path("B", listOf(Link("B", "A", 3.0), Link("A", "C", 1.0)))
                path.weight shouldBe 4.0
            }

        }

    }


})
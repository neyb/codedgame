import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import java.lang.NullPointerException
import java.util.*

class `le labyrinthe` : FreeSpec({
    "parsing" - {
        "3x3 spaces" {
            val playMap = """
                            ...
                            ...
                            ...
            """.parse()

            (0..2).flatMap { x -> (0..2).map { x to it } }.forEach { (x, y) ->
                playMap[x, y]!!.cellType shouldBe CellType.space
            }

        }
    }

    "closests" - {

        val `3x3 empty` = """
                            ...
                            ...
                            ...
            """.parse()

        "1 answer" {
            val closests = `3x3 empty`.closests(Position(0, 0)) { it == Position(2, 2) }
            closests.map { it.position } shouldContainExactlyInAnyOrder setOf(Position(2, 2))
        }

        "1 answer (the other is too far)" {
            val closests = `3x3 empty`.closests(Position(0, 0)) { it == Position(2, 2) || it == Position(1, 1) }
            closests.map { it.position } shouldContainExactlyInAnyOrder setOf(Position(1, 1))
        }


        "2 answer (others are too far)" {
            val closests = `3x3 empty`.closests(Position(0, 0)) { it.x == 2 || it.y == 2 }
            closests.map { it.position } shouldContainExactlyInAnyOrder setOf(Position(2, 0), Position(0, 2))
        }

    }
})

fun String.parse() = with(trimIndent()) {
    Scanner(this).parsePlayMap(lines().size)
}


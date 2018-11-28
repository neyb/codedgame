import java.lang.IllegalArgumentException
import java.util.Scanner

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val nbRows = input.nextInt()
    val nbColumns = input.nextInt()
    val doorClosingDelay = input.nextInt()

    while (true) {
        val kirkPosition = input.nextPosition()
        val playMap = input.parsePlayMap(nbRows)

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("RIGHT") // Kirk's next move (UP DOWN LEFT or RIGHT).
    }
}

fun Scanner.nextPosition() = Position(nextInt(), nextInt())

fun Scanner.parsePlayMap(nbRows: Int): PlayMap =
    (0 until nbRows)
        .map { it to next() }
        .flatMap { (y, line) ->
            line.mapIndexed { x, char ->
                Position(x, y) to CellType.of(char)
            }
        }
        .toMap(HashMap())
        .let(::PlayMap)

class PlayMap(
    val cellsByPosition: MutableMap<Position, CellType>
) {
    operator fun get(x: Int, y: Int) = cellsByPosition[Position(x, y)]

    fun closests(from: Position, predicate: (Position) -> Boolean): Collection<Position> =
        closests(setOf(from), HashSet(), predicate)

    private tailrec fun closests(
        from: Set<Position>,
        explored: MutableSet<Position>,
        test: (Position) -> Boolean
    ): Collection<Position> = when {
        from.isEmpty() -> from
        from.any(test) -> from.filter(test)
        else -> {
            explored += from
            val next = from.asSequence()
                .flatMap { it.neightboursSeq() }
                .filter { !explored.contains(it) }
                .toSet()
            closests(next, explored, test)
        }
    }

    private fun Position.neightboursSeq() = Direction.values().asSequence()
        .map { it.from(this) }
        .filter { cellsByPosition[it] != null }
}

enum class CellType {
    space, wall, door, console, unknown;

    companion object {
        fun of(c: Char): CellType = when (c) {
            '.' -> space
            '#' -> wall
            'T' -> door
            'C' -> console
            '?' -> unknown
            else -> throw IllegalArgumentException("$c is not a suported character")
        }
    }
}

data class Position(val x: Int, val y: Int)

data class TaggedNode<T>(val pos: Position, var tag: T)

enum class Direction(val xDiff: Int, val yDiff: Int) {
    up(0, -1), down(0, 1), left(-1, 0), right(1, 0);

    fun from(pos: Position) = Position(pos.x + xDiff, pos.y + yDiff)
}

enum class ExploreStatus {
    unexplored, exploring, explored
}


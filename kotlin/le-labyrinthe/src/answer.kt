import graph.*
import java.util.*

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
    (0 until nbRows).asSequence()
        .map { it to next() }
        .flatMap { (y, line) ->
            line.asSequence().mapIndexed { x, char ->
                Cell(Position(x, y), CellType.of(char))
            }
        }
        .toList()
        .let(::PlayMap)

class PlayMap(cells: Iterable<Cell>) {
    private val cellByPosition = cells.map { it.position to it }.toMap()

    private fun graph() = Graph(cellByPosition.values) { cell ->
        Direction.values().asSequence()
            .map { dir -> dir.from(cell.position) }
            .mapNotNull { cellByPosition[it] }
            .toSet()
    }

    operator fun get(x: Int, y: Int): Cell? = cellByPosition[Position(x, y)]

    fun closests(position: Position, that: (Position) -> Boolean): Collection<Cell> =
        graph().bfsClosests(cellByPosition[position]!!) { that(it.position) }
}

data class Cell(val position: Position, val cellType: CellType)

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

enum class Direction(val xDiff: Int, val yDiff: Int) {
    up(0, -1), down(0, 1), left(-1, 0), right(1, 0);

    fun from(pos: Position) = Position(pos.x + xDiff, pos.y + yDiff)
}


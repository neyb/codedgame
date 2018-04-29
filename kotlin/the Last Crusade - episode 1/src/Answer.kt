import Direction.*
import java.util.*
import kotlin.collections.HashMap

fun main(args: Array<String>) {

    val input = Scanner(System.`in`)
    val playMap = readPlaymap(input)
    val exitX = input.nextInt()

    // game loop
    while (true) {
        val position = Position(input.nextInt(), input.nextInt())
        val from = input.next()

        val room = playMap[position]
        val outDirection = room.out(Companion.parse(from))
        val out = position.at(outDirection)

        println("${out.x} ${out.y}")
    }
}

class PlayMap(private val rooms: Map<Position, Room>) {

    operator fun get(position: Position) = rooms[position]
            ?: throw IllegalArgumentException("no room at position $position")
}

data class Position(val x: Int, val y: Int) {

    fun at(direction: Direction): Position = when (direction) {
        top -> Position(x, y - 1)
        down -> Position(x, y + 1)
        right -> Position(x + 1, y)
        left -> Position(x - 1, y)
    }
}

enum class Direction {

    top, down, right, left;

    companion object {

        fun parse(input: String) = when (input) {
            "TOP" -> top
            "RIGHT" -> right
            "LEFT" -> left
            else -> throw IllegalArgumentException("no direction for $input")
        }
    }

    val opposite by lazy {
        when (this) {
            top -> down
            down -> top
            right -> left
            left -> right
        }
    }

}


class Room(vararg path: Pair<Direction, Direction>) {

    private val outByIn = path.toMap(HashMap())

    fun out(from: Direction): Direction = outByIn[from] ?: throw IllegalStateException("cannot come from $from")

}

private fun readPlaymap(input: Scanner): PlayMap {
    val rooms: Map<Int, Room> = mapOf(
            0 to Room(),
            1 to Room(top to down, left to down, right to down),
            2 to Room(left to right, right to left),
            3 to Room(top to down),
            4 to Room(top to left, right to down),
            5 to Room(top to right, left to down),
            6 to Room(left to right, right to left),
            7 to Room(top to down, right to down),
            8 to Room(left to down, right to down),
            9 to Room(top to down, left to down),
            10 to Room(top to left),
            11 to Room(top to right),
            12 to Room(right to down),
            13 to Room(left to down))

    val numberOfColumns = input.nextInt()
    val numberOflines = input.nextInt()

    if (input.hasNextLine()) input.nextLine()
    return PlayMap((0 until numberOflines).flatMap { y ->
        input.nextLine().split(' ')
                .map(String::toInt)
                .map { rooms[it] ?: throw IllegalArgumentException("no room for type $it") }
                .mapIndexed { x, room -> Position(x, y) to room }
    }.toMap(HashMap()))
}

import Direction.*
import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    var possibleLocation = PossibleLocation(
            top = 0, left = 0, right = input.nextInt() - 1, bottom = input.nextInt() - 1)
    @Suppress("UNUSED_VARIABLE")
    val turnLeft = input.nextInt()
    var batmanPosition = Point(input.nextInt(), input.nextInt())

    while (true) {
        val bombDir = Direction.of(input.next()) // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)

        possibleLocation = possibleLocation.at(batmanPosition, bombDir)
        batmanPosition = possibleLocation.mid

        with(batmanPosition) { println("$x $y") }
    }
}

enum class Direction(private val abbr: String) {
    Up("U"), UpRight("UR"), Right("R"), DownRight("DR"), Down("D"), DownLeft("DL"), Left("L"), UpLeft("UL");

    companion object {
        fun of(abbre: String) = values().first { it.abbr == abbre }
    }
}

class PossibleLocation(
        private val top: Int,
        private val left: Int,
        private val bottom: Int,
        private val right: Int
) {
    val mid: Point get() = Point((left + right) / 2, (top + bottom) / 2)

    fun at(batmanPosition: Point, bombDir: Direction) = with(batmanPosition) {
        when (bombDir) {
            Up -> PossibleLocation(top, x, y - 1, x)
            UpRight -> PossibleLocation(top, x + 1, y - 1, right)
            Right -> PossibleLocation(y, x + 1, y, right)
            DownRight -> PossibleLocation(y + 1, x + 1, bottom, right)
            Down -> PossibleLocation(y + 1, x, bottom, x)
            DownLeft -> PossibleLocation(y + 1, left, bottom, x - 1)
            Left -> PossibleLocation(y, left, y, x - 1)
            UpLeft -> PossibleLocation(top, left, y - 1, x - 1)
        }
    }
}

data class Point(val x: Int, val y: Int)
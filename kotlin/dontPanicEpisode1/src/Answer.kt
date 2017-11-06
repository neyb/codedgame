package dontPanicEpisode1

import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {

    fun Scanner.readMapPosition() = MapPosition(nextInt(), nextInt())

    val input = Scanner(System.`in`)
    val nbFloors = input.nextInt() // number of floors
    val width = input.nextInt() // width of the area
    val map = Map(List(nbFloors) { floor(width) })

    val nbRounds = input.nextInt() // maximum number of rounds

    map.setExit(input.readMapPosition())

    val nbTotalClones = input.nextInt() // number of generated clones

    val nbAdditionalElevators = input.nextInt() // ignore (always zero)

    (0 until input.nextInt()).forEach { map.addElevator(input.readMapPosition()) }

    // game loop
    while (true) {
        val cloneFloor = input.nextInt() // floor of the leading clone
        val clonePos = input.nextInt() // position of the leading clone on its floor
        val direction = input.next() // direction of the leading clone: LEFT or RIGHT

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("WAIT") // action: WAIT or BLOCK
    }
}

class Map(val floors: List<floor>) {
    fun setExit(exitPosition: MapPosition) {
        floors[exitPosition.floor].exit = exitPosition.position
    }

    fun addElevator(elevatorPosition: MapPosition) {
        floors[elevatorPosition.floor].elevators.add(elevatorPosition.position)
    }
}

class floor(
        val width: Int,
        val elevators:MutableList<Position> = mutableListOf(),
        var exit: Position? = null
) {

}

class Leader(
        val position: MapPosition,
        val direction:Direction
)

enum class Direction {

}

typealias Position = Int

class MapPosition(val floor: Int, val position: Position) {

}

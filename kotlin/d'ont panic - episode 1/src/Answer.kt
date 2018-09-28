import Direction.*
import java.util.*

fun main(args: Array<String>) {

    fun Scanner.readMapPosition() = MapPosition(nextInt(), nextInt())

    val input = Scanner(System.`in`)
    val nbFloors = input.nextInt() // number of floors
    val width = input.nextInt() // width of the area
    val map = Map(List(nbFloors) { floor(width) })

    input.nextInt() // maximum number of rounds

    map.setExit(input.readMapPosition())

    input.nextInt() // number of generated clones

    input.nextInt() // ignore (always zero)

    (0 until input.nextInt()).forEach { map.addElevator(input.readMapPosition()) }

    // game loop
    while (true) {
        val floor = input.nextInt()
        val position = input.nextInt()
        val direction = input.next()
        if (direction != "NONE") {
            val leader = Leader(MapPosition(floor, position), Direction.of(direction))

            System.err.println("$leader")

            println(map.orderFor(leader))
        } else{
            System.err.println("pas de leader")
            println(Order.WAIT)
        }
    }
}

class Map(val floors: List<floor>) {
    fun setExit(exitPosition: MapPosition) {
        floors[exitPosition.floor].exit = exitPosition.position
    }

    fun addElevator(elevatorPosition: MapPosition) {
        floors[elevatorPosition.floor].elevators.add(elevatorPosition.position)
    }

    fun orderFor(leader: Leader) = with(leader) {
        if (floors[position.floor].isGoingToRightDirection(position.position, direction))
            Order.WAIT
        else Order.BLOCK
    }
}

class floor(
        val width: Int,
        val elevators: MutableList<Position> = mutableListOf(),
        var exit: Position? = null) {

    fun isGoingToRightDirection(position: Position, direction: Direction) =
            if (exit != null) {
                System.err.println("exit in position $exit")
                val goodDirection = exit!!.isAt(direction, position)
                System.err.println("going in good direction ? $goodDirection")
                goodDirection
            }
            else {
                System.err.println("elevators in positions $elevators")
                val goodDirection = elevators.any { it.isAt(direction, position) }
                System.err.println("going in good direction ? $goodDirection")
                goodDirection
            }

    fun Position.isAt(direction: Direction, other: Position) = when (direction) {
        left -> this <= other
        right -> this >= other
    }
}

data class Leader(val position: MapPosition, val direction: Direction)

enum class Direction {
    left, right;

    companion object {
        fun of(name: String) = values().first { it.name == name.toLowerCase() }
    }
}

enum class Order { WAIT, BLOCK }

typealias Position = Int

data class MapPosition(val floor: Int, val position: Position)
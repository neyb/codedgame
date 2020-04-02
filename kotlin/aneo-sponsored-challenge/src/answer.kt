import java.util.*

typealias Speed = Int
typealias Distance = Int

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val road = Road.parse(input)
    println(road.fastestSpeed())
}

fun Speed.toMs() = this / 3.6
fun Distance.passAt(speed: Speed) = this * speed.toMs()

class Road {
    companion object {

        fun parse(scanner: Scanner): Road {
            val speed: Speed = input.nextInt()
            val lightCount = input.nextInt()
            for (i in 0 until lightCount) {
                val distance = input.nextInt()
                val duration = input.nextInt()
            }
            TODO()
        }
    }

    fun fastestSpeed(): Int {
        TODO("not implemented")
    }
}

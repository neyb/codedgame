import java.util.*

fun main() {
    Scanner(System.`in`).resolve(System.out::println)
}

fun Scanner.resolve(print: (String) -> Unit) {
    val maxSpeed = nextInt()
    val lights = (1..nextInt()).map { Light(nextInt(), nextInt()) }
    val foundOkMaxSpeed = generateSequence(maxSpeed) { (it - 1).takeIf { it > 0 } }
            .first { speed -> lights.all { light -> light.satisfy(speed) } }
    print(foundOkMaxSpeed.toString())
}

class Light(val distance: Int, val duration: Int) {
    fun satisfy(speed: Int): Boolean = distance * 36 / (speed * 10) % (2 * duration) < duration
}

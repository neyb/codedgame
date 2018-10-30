import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val horseNumber = input.nextInt()

    fun <T> Sequence<T>.successivePairs(): Sequence<Pair<T, T>> {
        var last: T? = null
        return this.mapNotNull { current ->
            val result = last?.let { current to it }
            last = current
            result
        }
    }

    println(
            (0 until horseNumber).asSequence()
                    .map { input.nextInt() }
                    .sorted()
                    .successivePairs()
                    .map { Math.abs(it.first - it.second) }
                    .min()
    )
}
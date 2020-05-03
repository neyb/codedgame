import java.util.*

fun main() = Scanner(System.`in`)
        .let(::encode)
        .let(::println)

internal fun encode(input: String) = encode(Scanner(input))

private fun encode(`in`: Scanner) = encode(`in`.nextLong())

private fun encode(n: Long) = if (n >= 0) encodePseudoPositive(n) else swap(encodePseudoPositive(-n))

private tailrec fun encodePseudoPositive(n: Long, suffix: String = ""): String =
        if (n <= 1) convertUnit(n) + suffix
        else {
            val rest = rest(n)
            encodePseudoPositive((n - rest) / 3, convertUnit(rest) + suffix)
        }

private fun rest(n: Long) = (n % 3).let { if (it == 2L) -1 else it }

private fun convertUnit(n: Long) = when (n) {
    0L -> "0"
    -1L -> "T"
    1L -> "1"
    else -> throw UnsupportedOperationException(n.toString() + " is not -1 / 0 / 1")
}

private fun swap(result: String) = result.map {
    when (it) {
        '1' -> 'T'
        'T' -> '1'
        else -> it
    }
}.joinToString("")

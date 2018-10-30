import java.util.*
import java.util.Objects.hash
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val c = Complexe.parse(input.nextLine())
    val max = input.nextInt()

    val lim = getLim(c, max)
    println(lim)
}

private fun getLim(c: Complexe, max: Int): Int {
    for (i in 1 until max)
        if (f(c, i).norme > 2.0) return i
    return max
}

private fun f(c: Complexe, n: Int): Complexe =
        if (n == 0) 0 + 0.i
        else f(c, n - 1).square + c

val Double.square: Double get() = Math.pow(this, 2.0)
val Number.i: Complexe get() = Complexe(imaginaire = this.toDouble())
operator fun Number.plus(c: Complexe) = with(c) { Complexe(reel + this@plus.toDouble(), imaginaire) }

class Complexe(reel: Double = 0.0, imaginaire: Double = 0.0) {

    val reel = reel.posZero
    val imaginaire = imaginaire.posZero

    companion object {
        fun parse(input: String): Complexe {
            val matchResult = Regex("""(-?\d+(?:\.\d+)?)([+\-]\d+(?:\.\d+)?)i""")
                    .matchEntire(input) ?: throw RuntimeException("patterne incorrect")

            val reel = matchResult.groupValues[1].toDouble()
            val imaginaire = matchResult.groupValues[2].toDouble()

            return reel + imaginaire.i
        }

        private val Double.posZero: Double get() = if (this == -0.0) 0.0 else this
    }

    val square: Complexe get() = (reel.square - imaginaire.square) + (2.0 * reel * imaginaire).i

    val norme: Double get() = Math.sqrt(Math.pow(reel, 2.0) + Math.pow(imaginaire, 2.0))

    operator fun plus(c: Complexe): Complexe = reel + c.reel + (imaginaire + c.imaginaire).i

    override fun hashCode() = hash(reel, imaginaire)

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other !is Complexe -> false
        else -> reel == other.reel && imaginaire == other.imaginaire
    }

    override fun toString() =
            "$reel + ${imaginaire}i"
}
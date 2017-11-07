import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)



    (0 until input.nextInt())
            .map { input.next() }

            {
                val cardp1 =  // the n cards of player 1
            }
    val m = input.nextInt() // the number of cards for player 2
    for (i in 0 until m) {
        val cardp2 = input.next() // the m cards of player 2
    }

    // Write an action using println()
    // To debug: System.err.println("Debug messages...");

    println("PAT")
}

fun Scanner.readStack() =
        (0 until nextInt())
                .map { next() }
                .map { }

class Card(value)

enum class CardValue(private val label) {
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9"),
    ten("10"),
    jack("J"),
    queen("Q"),
    king("K"),
    ace("A");
    companion object {
        of(label:String)
    }
}
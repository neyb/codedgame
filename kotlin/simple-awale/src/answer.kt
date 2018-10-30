import java.util.*

val nbBowls = 7

fun main(args: Array<String>) {
    val input = if (args.size > 0) Scanner(args[0]) else Scanner(System.`in`)

    val playMap = PlayMap(
            opBowls = Bowls(input.nextLine().split(' ').map { it.toInt() }, false),
            myBowls = Bowls(input.nextLine().split(' ').map { it.toInt() }, true))
    val num = input.nextInt()

    System.err.println("before playing at $num")
    System.err.println(playMap.format())

    val replay = playMap.play(num)

    System.err.println("after")
    println(playMap.format())
    if (replay) println("REPLAY")


}

class PlayMap(val opBowls: Bowls, val myBowls: Bowls) {
    fun play(index: Int): Boolean {
        val hand = myBowls.takeGrains(index)

        var toMine = true
        var from = index + 1
        while (!hand.empty) {
            hand.playFrom(if (toMine) myBowls else opBowls, from)
            from = 0
            if (!hand.empty) toMine = !toMine
        }
        return toMine && hand.playedInAllBowls!!
    }

    fun format() = """
        ${opBowls.format()}
        ${myBowls.format()}
    """.trimIndent()
}

class Bowls(bowls: List<Int>, mine: Boolean) {
    val bowls = bowls.toMutableList()
    val lastPlayable = if (mine) nbBowls - 1 else nbBowls - 2

    fun takeGrains(index: Int) = Hand(bowls[index])
            .also { bowls[index] = 0 }

    fun add1(index: Int) {
        bowls[index] += 1
    }

    fun format() = bowls.withIndex().map { (index, nbGrain) ->
        if (index == nbBowls - 1) "[$nbGrain]"
        else nbGrain.toString()
    }.joinToString(" ")
}

class Bowl(var nbGrain: Int, val last: Boolean) {
    fun add() {
        nbGrain += 1
    }
}

class Hand(var nbGrains: Int) {
    var playedInAllBowls: Boolean? = null

    val empty: Boolean get() = nbGrains <= 0

    fun playFrom(bowls: Bowls, index: Int) {
        playedInAllBowls = false
        (index..bowls.lastPlayable).asSequence()
                .takeWhile { nbGrains > 0 }
                .forEach { bowlIndex ->
                    nbGrains -= 1
                    bowls.add1(bowlIndex)
                    playedInAllBowls = bowlIndex == bowls.lastPlayable
                }
    }
}

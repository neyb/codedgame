import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val NbWords = input.nextInt()
    if (input.hasNextLine()) input.nextLine()
    val words: Set<wordInfo<SetIncludable>> = (0 until NbWords).map { input.nextLine() }
            .map { word -> SetIncludable.wordOf(word) }
            .toSet()
    val letters = input.nextLine()
    val searched = SetIncludable.of(letters)

    val bestWord = words
            .asSequence()
            .sortedByDescending { it.point }
            .first { it.playable(searched) }
            .word

    println(bestWord)
}

val pointsByLetter = listOf(
        setOf('e', 'a', 'i', 'o', 'n', 'r', 't', 'l', 's', 'u') to 1,
        setOf('d', 'g') to 2,
        setOf('b', 'c', 'm', 'p') to 3,
        setOf('f', 'h', 'v', 'w', 'y') to 4,
        setOf('k') to 5,
        setOf('j', 'x') to 8,
        setOf('q', 'z') to 10)
        .flatMap { (set, v) -> set.map { it to v } }
        .toMap()

class wordInfo<T : Includable<T>>(
        val word: String,
        val index: Includable<T>
) {
    val point = word.sumBy { pointsByLetter[it]!! }

    fun playable(search: T): Boolean = index.include(search)
}

interface Includable<T : Includable<T>> {
    fun include(other: T): Boolean
}

class SetIncludable(
        private val set: Set<CharQty>
) : Includable<SetIncludable> {
    companion object {
        fun of(word: String): SetIncludable = SetIncludable(
                word.asSequence()
                        .map { char -> CharQty(char, word.count { it == char }) }
                        .toSet()
        )
        fun wordOf(word: String): wordInfo<SetIncludable> = wordInfo(word, SetIncludable(
                word.asSequence()
                        .map { char -> CharQty(char, word.count { it == char }) }
                        .toSet()
        ))
    }

    override fun include(other: SetIncludable) = set.all { charQtyincluding ->
        other.set.any { charQtyIncluded -> charQtyincluding.include(charQtyIncluded) }
    }
}

data class CharQty(
        val char: Char,
        val qty: Int
) {
    fun include(other: CharQty) = char == other.char && qty <= other.qty
}
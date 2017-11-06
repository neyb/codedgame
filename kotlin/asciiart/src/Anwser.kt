package asciiart

import java.io.*
import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt()
    val high = input.nextInt()
    if (input.hasNextLine()) input.nextLine()
    val text = input.nextLine()

    val printer = Printer(width, high)
    printer.load(input)
    printer.print(text, System.out)
}

class Printer(private val width: Int, private val high: Int) {

    companion object {
        val desc: String by lazy {
            val descBuilder = StringBuilder(26)
            for (char in 'a'..'z') descBuilder.append(char)
            descBuilder.append('?').toString()
        }
    }

    private val letters = mutableMapOf<Char, MutableMap<Int, String>>()

    fun load(input: Scanner) {
        for (lineIndex in 0 until high)
            loadLine(lineIndex, input.nextLine())
    }

    private fun loadLine(lineIndex: Int, line: String) {
        val representationsByLetter = desc.toList().zip(line.by(width)).toMap()
        for (letter in desc)
            this[letter, lineIndex] = representationsByLetter[letter]!!
    }

    fun print(text: String, out: PrintStream) {
        for (lineIndex in 0 until high) {
            for (letter in text)
                out.print(this[letter, lineIndex])
            out.println()
        }
    }

    private operator fun set(char: Char, lineIndex: Int, asciiLine: String) {
        char.representation()[lineIndex] = asciiLine
    }

    private operator fun get(char: Char, lineIndex: Int) = char.representation()[lineIndex]

    private fun Char.representation() =
            letters.computeIfAbsent(this.normalized) { mutableMapOf() }

    private val Char.normalized: Char
        get() = when (this) {
            in 'a'..'z' -> this
            in 'A'..'Z' -> this.toLowerCase()
            else -> '?'
        }

    private fun String.by(width: Int) = (0 until length step width)
            .map { index -> substring(index, minOf(length, index + width)) }
}

import java.io.PrintStream
import java.util.*

fun main() {
    Scanner(System.`in`).resolve(System.out)
}

fun Scanner.resolve(printStream: PrintStream) {
    val cells = (0 until nextInt()).map { readCell() }
    cells.forEach { it.cells = cells }

    cells.forEach { printStream.println(it.value) }
}

fun Scanner.readCell() = Cell.parse(next(), next(), next())

class Cell(val operation: Operation, val arg1: Arg, val arg2: Arg) {
    companion object {
        fun parse(operation: String, arg1: String, arg2: String) =
                Cell(Operation.parse(operation), Arg.parse(arg1), Arg.parse(arg2))
    }

    lateinit var cells: List<Cell>

    val value by lazy { operation({ arg1.value(cells) }, { arg2.value(cells) }) }
}

enum class Operation(val key: String, apply: (() -> Int, () -> Int) -> Int) : (() -> Int, () -> Int) -> Int by apply {
    value("VALUE", { arg1, _ -> arg1() }),
    add("ADD", { arg1, arg2 -> arg1() + arg2() }),
    sub("SUB", { arg1, arg2 -> arg1() - arg2() }),
    mult("MULT", { arg1, arg2 -> arg1() * arg2() });

    companion object {
        fun parse(s: String) = values().single { it.key == s }
    }
}

interface Arg {
    fun value(cells: List<Cell>): Int

    companion object {
        fun parse(s: String): Arg {
            return when {
                s == "_" -> NoArg
                s.startsWith("$") -> Ref(s.substring(1).toInt())
                s.matches("""^-?\d+$""".toRegex()) -> Value(s.toInt())
                else -> throw Exception("cannot parse $s")
            }
        }
    }

    class Value(val value: Int) : Arg {
        override fun value(cells: List<Cell>) = value
    }

    class Ref(val cellIndex: Int) : Arg {
        override fun value(cells: List<Cell>) = cells[cellIndex].value
    }

    object NoArg : Arg {
        override fun value(cells: List<Cell>) = throw Exception("no arg has no value")
    }
}

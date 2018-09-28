import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val numberOfLine = input.nextInt()
    if (input.hasNextLine()) input.nextLine()

    val lineSequence = (1..numberOfLine).asSequence().map { input.nextLine() }

    println(if (lineSequence.isValid()) "VALID" else "INVALID")
}

fun Sequence<String>.isValid(): Boolean {
    val moleculeParser = tokens()
            .fold(MoleculeBuilder()) { moleculeBuilder, (position, token) ->
                moleculeBuilder.apply { add(position, token) }
            }

    val valid = moleculeParser.carbonUnits().all { it -> it.isValid() }
    return valid
}

fun Sequence<String>.tokens(): Sequence<Pair<Position, String>> =
        withIndex().flatMap { (lineIndex, line) ->
            line.asSequence()
                    .chunked(3)
                    .map { it.joinToString("") }
                    .withIndex()
                    .map { (colIndex, token) -> Position(colIndex, lineIndex) to token }
        }

data class Position(val x: Int, val y: Int) {
    val top by lazy { Position(x, y - 1) }
    val bottom by lazy { Position(x, y + 1) }
    val left by lazy { Position(x - 1, y) }
    val right by lazy { Position(x + 1, y) }
}

class MoleculeBuilder {

    companion object {
        private val carbonUnitPattern = Regex("CH(?<HNum>\\d)")
        private val bondPattern = Regex("\\((?<strength>\\d)\\)")
    }

    private val carbonUnits = mutableMapOf<Position, CarbonUnit>()
    private val bonds = mutableSetOf<Bond<Position>>()

    fun add(position: Position, value: String) {
        carbonUnitPattern.matchEntire(value)?.run {
            carbonUnits += position to CarbonUnit(groupValues[1].toInt())
        }

        bondPattern.matchEntire(value)?.run {
            val strength = groupValues[1].toInt()
            bonds +=
                    if (position.y % 2 == 0) Bond(strength, position.left, position.right)
                    else Bond(strength, position.top, position.bottom)
        }
    }

    fun carbonUnits(): Collection<CarbonUnit> {
        val resultByPosition = carbonUnits.mapValues { CarbonUnit(it.value.hNum) }

        bonds.map {
            Bond(
                    it.strength,
                    resultByPosition[it.first] ?: throw Exception("nothing at ${it.first}"),
                    resultByPosition[it.second] ?: throw Exception("nothing at ${it.second}"))
        }
                .forEach { bond -> bond.register() }

        return resultByPosition.values
    }

    fun Bond<CarbonUnit>.register() {
        first.add(this)
        second.add(this)
    }
}

class CarbonUnit(val hNum: Int) {
    private val bonds = mutableSetOf<Bond<CarbonUnit>>()

    fun add(bond: Bond<CarbonUnit>) {
        bonds.add(bond)
    }

    fun isValid() = hNum + bonds.sumBy { it.strength } == 4
}

data class Bond<T>(val strength: Int, val first: T, val second: T)

fun <T> Sequence<T>.chunked(size: Int): Sequence<List<T>> {
    val iterator = iterator()

    return generateSequence {
        if (iterator.hasNext()) (1..size).asSequence()
                .takeWhile { iterator.hasNext() }
                .map { iterator.next() }
                .toList()
        else null
    }
}
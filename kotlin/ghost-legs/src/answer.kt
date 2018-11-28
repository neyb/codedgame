import java.util.*

fun main(args: Array<String>) {
    Scanner(System.`in`)
            .let(::parse)
            .let(Legs::print)
}

private fun parse(input: Scanner): Legs {
    @Suppress("UNUSED_VARIABLE") val width = input.nextInt()
    val height = input.nextInt()
    if (input.hasNextLine()) input.nextLine()

    return (0 until height).map { input.nextLine() }
            .foldIndexed(Legs.Builder()) { lineIndex, builder, line ->
                builder.apply {
                    when (lineIndex) {
                        0 -> builder.top = line.split("  ").map { it[0] }
                        height - 1 -> builder.bottom = line.split("  ").map { it[0] }
                        else -> builder.links.addAll(line.parseLinks())
                    }
                }
            }.build()
}

private fun String.parseLinks(): List<Link> =
        "--".toRegex().findAll(this)
                .map { (it.range.first - 1) / 3 }
                .map(::Link)
                .toList()

class Legs(
        top: List<Char>,
        links: List<Link>,
        bottom: List<Char>
) {
    class Builder {
        var top: List<Char>? = null
        val links: MutableList<Link> = mutableListOf()
        var bottom: List<Char>? = null

        operator fun plus(link: Link) {
            links.add(link)
        }

        fun build() = Legs(top!!, links, bottom!!)
    }

    val paths: Collection<Path> = top.mapIndexed { index, top -> top to bottom[links.leadTo(index)] }

    fun List<Link>.leadTo(fromIndex: Int) = fold(fromIndex) { from, link -> link.follow(from) }

    fun print() = paths.forEach { println("${it.from}${it.to}") }
}

infix fun Char.to(to: Char) = Path(this, to)

data class Path(val from: Char, val to: Char)

class Link(val from: Int) {
    val to: Int get() = from + 1

    fun follow(fromIndex: Int) = when (fromIndex) {
        from -> to
        to -> from
        else -> fromIndex
    }
}
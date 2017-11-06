package thereisnospoonEp1

import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val width = input.nextInt()
    val height = input.nextInt()

    input.endLine()

    val nodes = (0 until height)
            .map { it to input.nextLine().indexesOf('0') }
            .flatMap { pair -> pair.second.map { pair.first to it } }
            .map { Node(it.second, it.first) }

    Grid(nodes.toSet()).consumeGroups { (node, right, bottom) -> println("$node $right $bottom") }
}

private fun Scanner.endLine() {
    if (hasNextLine()) nextLine()
}

private fun String.indexesOf(char: Char): List<Int> {
    var searchFrom = 0
    return generateSequence {
        val foundIndex = indexOf(char, searchFrom)
        System.err.println(foundIndex)
        searchFrom = foundIndex + 1
        if (foundIndex >= 0) foundIndex else null
    }.toList()
}

data class Node(val x: Int, val y: Int) {
    companion object {
        val fake = Node(-1, -1)
    }

    override fun toString() = "$x $y"
}

data class Group(val node: Node, val right: Node, val bottom: Node)

class Grid(private val nodes: Set<Node>) {
    private val nodesByX = nodes.groupBy { it.x }
    private val nodesByY = nodes.groupBy { it.y }

    fun consumeGroups(block: (Group) -> Unit) {
        nodes.map { Group(it, it.right ?: Node.fake, it.bottom ?: Node.fake) }.forEach(block)
    }

    private val Node.right get() = nodesByY[y]?.let { it.asSequence().filter { it.x > x }.minBy { it.x } }
    private val Node.bottom get() = nodesByX[x]?.let { it.asSequence().filter { it.y > y }.minBy { it.y } }
}
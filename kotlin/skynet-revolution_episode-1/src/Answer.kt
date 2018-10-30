import java.util.*
import kotlin.Comparator

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val level = Level(input.nextInt())
    val nbLinks = input.nextInt()
    val nbGateways = input.nextInt()

    for (i in 0 until nbLinks) level.addLink(input.nextInt(), input.nextInt())

    for (i in 0 until nbGateways) level.addGateway(input.nextInt())

    while (true) {
        val skynetIndex = input.nextInt()

        val linkToCut = level.getMostCriticalLink(skynetIndex) ?: level.anyLink() ?: throw RuntimeException("no link to cut")

        level.cut(linkToCut)
        println("${linkToCut.node1.num} ${linkToCut.node2.num}")
    }
}

class Level(nbNodes: Int) {
    val nodes = List(nbNodes) { Node(it) }
    val gateways: List<Node>
        get() = nodes.filter { it.gateway == true }

    fun addLink(fromIndex: Int, toIndex: Int) {
        nodes[fromIndex].linkTo(nodes[toIndex])
        nodes[toIndex].linkTo(nodes[fromIndex])
    }

    fun addGateway(index: Int) {
        nodes[index].gateway = true
    }

    fun cut(link: Link) {
        nodes[link.node1.num].cut(link)
        nodes[link.node2.num].cut(link)
    }

    fun getMostCriticalLink(skynetIndex: Int): Link? {
        update(skynetIndex)
        return nodes.flatMap { it.links }
                .filter { !it.protected }
                .minWith(Comparator.comparing<Link, Int> { it.criticity }
                        .thenComparing(Comparator.comparing<Link, Int> { it.distance }))
    }

    fun anyLink(): Link? {
        return nodes.firstOrNull()?.links?.first()
    }

    private fun update(skynetIndex: Int) {
        updateDanger()
        updateSkynetDistance(skynetIndex)
    }

    private fun updateDanger() {
        nodes.forEach { it.danger = null }
        bfsWalk(gateways) { updateDanger() }
    }

    private fun updateSkynetDistance(skynetIndex: Int) {
        nodes.forEach { it.skynetDistance = null }
        bfsWalk(listOf(nodes[skynetIndex])) { updateSkynetDistance() }
    }

    private fun bfsWalk(from: Iterable<Node>, block: Node.(Set<Node>) -> Unit) {
        val done = mutableSetOf<Node>()
        var current = from.toSet()

        while (current.isNotEmpty()) {
            for (node in current) {
                node.block(current)
            }

            val tmp = current
            current = current.flatMap { it.linkedNodes }.filter { !done.contains(it) }.toSet()
            done.addAll(tmp)
        }
    }

}

class Node(val num: Int) {
    val linkedNodes = mutableSetOf<Node>()
    var gateway = false
    var danger: Int? = null
    var skynetDistance: Int? = null

    val links: Set<Link> get() = linkedNodes.map { Link(this, it) }.toSet()

    val criticity: Int get() = danger!! + skynetDistance!!

    fun linkTo(other: Node) {
        linkedNodes += other
    }

    fun cut(link: Link) {
        take(link)?.also { linkedNodes -= it }
    }

    fun updateDanger() {
        val closeDangers = linkedNodes
                .filter { it.danger != null }
                .map { it.danger!! }

        danger = if (closeDangers.isEmpty()) 0
        else closeDangers.reduce { acc, i -> if (acc == 1 && i == 1) 0 else minOf(acc, i) } + 1
    }

    fun updateSkynetDistance() {
        val closeDistances = linkedNodes
                .filter { it.skynetDistance != null }
                .map { it.skynetDistance!! }
        skynetDistance = if (closeDistances.isEmpty()) 0
        else closeDistances.reduce { acc, i -> minOf(acc, i) } + 1
    }

    private fun take(link: Link) = when (this) {
        link.node1 -> link.node2
        link.node2 -> link.node1
        else -> null
    }

    override fun hashCode() = num

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Node -> false
        else -> num == other.num
    }

    override fun toString() = "$num"
}

class Link(val node1: Node, val node2: Node) {
    val criticity: Int get() = node1.criticity + node2.criticity

    val distance: Int get() = minOf(node1.skynetDistance!!, node2.skynetDistance!!)

    val protected: Boolean get() = !(node1.gateway || node2.gateway)

    override fun hashCode() = node1.hashCode() + node2.hashCode()

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Link -> false
        else -> (node1 == other.node1 && node2 == other.node2) || (node1 == other.node2 && node2 == other.node1)
    }
    override fun toString() = "${node1.num} <-> ${node2.num}"
}
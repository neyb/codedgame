import java.util.Scanner

fun main(args: Array<String>) = Scanner(System.`in`).parseGraph().minDepth().let(::println)

fun Scanner.parseGraph() = nextInt()
        .let { 0 until it }
        .map { Link(nextInt(), nextInt()) }
        .toMutableSet()
        .let(::Graph)

class Link(val from: Int, val to: Int) {
    override fun hashCode() = from + to

    override fun equals(other: Any?) = when (other) {
//        other === this -> true // this cause a bug
        !is Link -> false
        else -> (from == other.from && to == other.to) || (from == other.to && to == other.from)
    }

    override fun toString() = "$from<->$to"
}

open class Graph(private val links: MutableSet<Link>) {

    constructor(vararg pairs: Pair<Int, Int>) :
            this(pairs.map { (from, to) -> Link(from, to) }.toMutableSet())

    open protected val paths = links.asSequence()
            .flatMap { sequenceOf(it.from to it.to, it.to to it.from) }
            .grouped()

    open protected val nodeByLinkNb: MutableMap<Int, MutableSet<Int>> = paths.asSequence()
            .map { (node, to) -> to.size to node }
            .grouped()

    private fun <K, V> Sequence<Pair<K, V>>.grouped(): MutableMap<K, MutableSet<V>> =
            fold(HashMap()) { map, (k, v) -> map.apply { computeIfAbsent(k) { HashSet() } += v } }

    fun leafs(): Iterable<Int> = nodeByLinkNb[1]!!.toSet()

    fun minDepth(): Int = Graph(HashSet(links)).minDepth(0)

    private fun removeLeafs() {
        leafs().forEach { leaf ->
            paths.remove(leaf)?.forEach { to ->
                val leadsFromTo = removeFromPaths(to, leaf)
                decreaseNodeByLinkNbFor(to, leadsFromTo.size)
            }
        }
    }

    private fun removeFromPaths(to: Int, leaf: Int): MutableSet<Int> {
        val leadsFromTo = paths[to]!!
        leadsFromTo.remove(leaf)
        if (leadsFromTo.isEmpty()) paths.remove(to)
        return leadsFromTo
    }

    private fun decreaseNodeByLinkNbFor(to: Int, targetSize: Int) {
        nodeByLinkNb[targetSize + 1]!!.remove(to)
        if (targetSize >= 0) nodeByLinkNb.computeIfAbsent(targetSize) { HashSet() }.add(to)
    }

    tailrec fun minDepth(depth: Int): Int =
            if (paths.isEmpty()) depth
            else {
                removeLeafs()
                minDepth(depth + 1)
            }

    override fun toString() = paths.toString()
}
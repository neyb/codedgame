package graph

import java.lang.IllegalArgumentException

class Link<Node>(
    val from: Node,
    val to: Node,
    val weight: Double = 1.0
) {
    fun from(node: Node) = when (node) {
        this.from -> this
        to -> Link(to, from, weight)
        else -> throw IllegalArgumentException("$node is not in $this")
    }

    operator fun get(from: Node): Node? = when (from) {
        this.from -> to
        to -> from
        else -> null
    }

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Link<*> -> false
        other.weight != weight -> false
        else -> other.from == from && other.to == to
                || other.to == from && other.from == to
    }

    override fun hashCode() = from.hashCode() + to.hashCode() + 31 * weight.toInt()

    override fun toString() = "$from<-<$weight>->$to"
}
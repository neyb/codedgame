package graph

data class Path<Node>(
    val start: Node,
    val links: List<Link<Node>>
) {
    val end: Node get() = links.lastOrNull()?.to ?: start

    val weight: Double get() = links.sumByDouble { it.weight.toDouble() }

    fun append(link: Link<Node>): Path<Node> = Path(start, links + link)
}
package graph

import collections.minsBy

class Graph<Node : Any> private constructor(nodes: Iterable<Node>, links: Iterable<Link<Node>>) {

    constructor(additionalNodes: Collection<Node>, vararg links: Link<Node>) : this(additionalNodes, links.toSet())
    constructor(vararg links: Link<Node>) : this(emptySet(), *links)

    constructor(nodes: Iterable<Node>, links: (Node) -> Set<Node>) : this(
        nodes,
        nodes.flatMap { from -> links(from).map { to -> Link(from, to) } }.toSet()
    )

    private val linksByNode: Map<Node, Set<Link<Node>>> =
        (nodes.toSet() + links.flatMap { listOf(it.from, it.to) }).asSequence()
            .map { node -> node to links.filter { it.from == node || it.to == node }.toSet() }
            .toMap()

    fun bfsClosests(from: Node, predicate: (Node) -> Boolean): Collection<Node> =
        bfsClosests(setOf(from), HashSet(), predicate)

    private tailrec fun bfsClosests(
        from: Set<Node>,
        explored: MutableSet<Node>,
        test: (Node) -> Boolean
    ): Collection<Node> = when {
        from.isEmpty() -> from
        from.any(test) -> from.filter(test)
        else -> {
            explored += from
            val next = from.asSequence()
                .flatMap { node ->
                    linksByNode[node]?.asSequence()
                        ?.mapNotNull { it[node] }
                        ?: emptySequence()
                }
                .filter { !explored.contains(it) }
                .toSet()
            bfsClosests(next, explored, test)
        }
    }

    fun findPath(from: Node, to: Node, heuristic: (Node) -> Double = { 0.0 }): Collection<Path<Node>> {
        return findPath(
            scoredPaths = ScoredPaths.fromNode(from) { path: Path<Node> -> path.weight + heuristic(path.end) },
            visited = HashSet(),
            to = to
        )
    }

    private tailrec fun findPath(
        scoredPaths: ScoredPaths<Node>,
        visited: MutableSet<Node>,
        to: Node
    ): Collection<Path<Node>> {
        val fromsWithShortestPath = scoredPaths.closestNodeWithPath(visited)
        visited += fromsWithShortestPath.keys

        for ((from, pathsToFrom) in fromsWithShortestPath) {
            if (from == to) return pathsToFrom

            val links = linksByNode[from]
                ?.map { it.from(from) }
                ?.filter { it.to !in visited }
                ?: emptyList()

            if (links.isEmpty()) return emptyList()

            links.forEach { link ->
                val newPaths = pathsToFrom.map { it.append(link) }
                newPaths.forEach { newPath -> scoredPaths[link.to] = newPath }
            }
        }

        return findPath(scoredPaths, visited, to)
    }

    private class ScoredPaths<Node>(private val scorer: (Path<Node>) -> Double) {
        private val bestPathsByNode = HashMap<Node, BestPathsAndScore<Node>>()

        companion object {
            fun <Node> fromNode(from: Node, scorer: (Path<Node>) -> Double): ScoredPaths<Node> =
                ScoredPaths(scorer).apply { set(from, Path(from, emptyList())) }
        }

        operator fun set(node: Node, path: Path<Node>) {
            val bestPathsAndScore = bestPathsByNode[node]
            if (bestPathsAndScore == null) bestPathsByNode[node] = BestPathsAndScore.of(path, scorer)
            else bestPathsAndScore.consider(path)
        }

        fun closestNodeWithPath(ignoring: Collection<Node>): Map<Node, Collection<Path<Node>>> {
            return bestPathsByNode.values
                .filter { it.to !in ignoring }
                .minsBy { it.score }
                .map { it.to to it.paths }.toMap()
        }
    }

    private class BestPathsAndScore<Node>(
        val to: Node,
        var score: Double,
        val paths: MutableSet<Path<Node>>,
        private val scorer: (Path<Node>) -> Double
    ) {
        companion object {
            fun <Node> of(path: Path<Node>, scorer: (Path<Node>) -> Double): BestPathsAndScore<Node> =
                BestPathsAndScore(path.end, scorer(path), hashSetOf(path), scorer)
        }

        fun consider(newPath: Path<Node>) {
            val newPathScore = scorer(newPath)
            when {
                score == newPathScore -> paths += newPath
                score > newPathScore -> {
                    score = newPathScore
                    paths.clear()
                    paths.add(newPath)
                }
            }
        }
    }
}



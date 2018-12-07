package collections

inline fun <T, C : Comparable<C>> Iterable<T>.minsBy(toCompare: (T) -> C): Collection<T> {
    val mins = ArrayList<T>()
    var min: C? = null

    for (current in this) {
        val toCompare = toCompare(current)
        when {
            min == null -> {
                min = toCompare
                mins += current
            }
            min == toCompare -> mins += current
            min > toCompare -> {
                min = toCompare
                mins.clear()
                mins += current
            }
        }
    }

    return mins
}
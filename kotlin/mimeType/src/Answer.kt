import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val tableSize = input.nextInt()
    val answerSize = input.nextInt()
    val mappings = (0 until tableSize).asSequence()
            .map { input.next().toLowerCase() to input.next() }
            .toMap()
    input.nextLine()

    (0 until answerSize)
            .map { input.nextLine() }
            .map { filename ->
                filename to (mappings[filename.substringAfterLast('.', "").toLowerCase()] ?: "UNKNOWN")
            }
            .forEach {
                System.err.println("${it.first} -> ${it.second}")
                println(it.second)
            }
}

class Mapping(extension: String, val mime: String) {
    val lowerCaseExtension = extension.toLowerCase()
    override fun toString() = "$mime:$lowerCaseExtension"
}
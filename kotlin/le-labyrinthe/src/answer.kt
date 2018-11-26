import java.util.Scanner

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val R = input.nextInt() // number of rows.
    val C = input.nextInt() // number of columns.
    val A = input.nextInt() // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.

    // game loop
    while (true) {
        val KR = input.nextInt() // row where Kirk is located.
        val KC = input.nextInt() // column where Kirk is located.
        for (i in 0 until R) {
            val ROW = input.next() // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("RIGHT") // Kirk's next move (UP DOWN LEFT or RIGHT).
    }
}
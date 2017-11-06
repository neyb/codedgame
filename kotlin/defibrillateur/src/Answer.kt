package defibrillateur

import java.util.*
import java.lang.Math.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val currentPosition = Position(input.next().toFrenchDouble(), input.next().toFrenchDouble())

    val N = input.nextInt()
    if (input.hasNextLine()) input.nextLine()

    val defibs = (0 until N)
            .map { input.nextLine() }
            .map { Defibrilateur.parse(it) }

    println(defibs.minBy { it.distanceTo(currentPosition) }?.name)
}

fun String.toFrenchDouble() = this.replace(',','.').toDouble()

class Position(val longitude: Double, val latitude: Double) {
    fun distanceTo(other: Position): Double {
        val x = (other.longitude - longitude) * cos((latitude + other.latitude) / 2)
        val y = other.latitude - latitude
        return sqrt(x * x + y * y) * 6371
    }
}

class Defibrilateur(val name: String, val position: Position) {
    companion object {
        fun parse(line: String): Defibrilateur {
            val splited = line.split(';')
            return Defibrilateur(splited[1], Position(splited[4].toFrenchDouble(), splited[5].toFrenchDouble()))
        }
    }

    fun distanceTo(position: Position) = this.position.distanceTo(position)
}
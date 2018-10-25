import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Test : FreeSpec({

    infix fun String.testEncodeTo(expected: String) {
        val input = this
        "$input is encoded to $expected" { encode(input) shouldBe expected }
    }
    
    "1" testEncodeTo "1"
    "0" testEncodeTo "0"
    "-1" testEncodeTo "T"
    "3" testEncodeTo "10"
    "9" testEncodeTo "100"
    "2" testEncodeTo "1T"
    "8" testEncodeTo "10T"
    "15" testEncodeTo "1TT0"
    "-15" testEncodeTo "T110"
    "-9223372036854775000" testEncodeTo "T1T1TTT00TTT001T0T11TT10T0T01010T0T1TT00T"
})
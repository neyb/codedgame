import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec

class `organic compound tests` : FreeSpec({


    sequenceOf(
            "CH3(1)CH3" to true,
            "CH3(2)CH3" to false,
            "CH2(2)CH2" to true,
            "CH2(1)CH2" to false,
            """
               CH2(1)CH3
               (1)
               CH3
            """.trimIndent() to true,
            """
               CH2(1)CH2
               (1)   (1)
               CH2(1)CH2
               """.trimIndent() to true,
            """
               CH2(1)CH1
               (1)   (1)
               CH2(1)CH2
               """.trimIndent() to false
    ).forEach { (molecule, valid) ->
        "${molecule} is ${if (valid) "valid" else "invalid"}" {
            molecule.lineSequence().isValid() shouldBe valid
        }
    }
})
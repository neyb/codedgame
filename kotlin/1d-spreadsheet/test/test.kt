import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

fun withStringPrintStream(block: (PrintStream) -> Unit): String {
    val out = ByteArrayOutputStream()
    block(PrintStream(out))
    return out.toString()
}

class `1d spreadsheet` : FreeSpec({
                                      "example" {

                                          val out = withStringPrintStream {
                                              Scanner("""2
                                                        |VALUE 3 _
                                                        |ADD $0 4
                                                        """.trimMargin()
                                                ).resolve(it)
                                          }

                                          out shouldBe """3
                                                         |7
                                                         |""".trimMargin()
                                      }
                                  })
import org.junit.jupiter.api.Test

class Test {

    @Test fun `azezae`() {
        val level = Level(3)
        level.addLink(0, 1)
        level.addLink(2, 1)
        level.addGateway(2)
        val mostCriticalLink = level.getMostCriticalLink(1)
        println(mostCriticalLink)
    }

}
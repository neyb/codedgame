import io.kotlintest.specs.FreeSpec

class `skynet revolution - episode 1` : FreeSpec({
    "azezae" {
        val level = Level(3)
        level.addLink(0, 1)
        level.addLink(2, 1)
        level.addGateway(2)
        val mostCriticalLink = level.getMostCriticalLink(1)
        println(mostCriticalLink)
    }
})
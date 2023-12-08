
fun main() {

    fun part1(input: List<String>): Int {
        val day02 = Day02(input)
        return day02.getValidGamesForGrab()
            .map { it -> it.id} .sum()
    }


    fun part2(input: List<String>): Int {
        val day02 = Day02(input)
        return day02.getPowerValueForPart2()
    }

// ###############################################################
    val input = readInput(
//        "Day02-example"
        "Day02-input"
//        "Day02-pt2-example"
//        "Day02-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

enum class Color(val value: String) {
    BLUE("blue"), RED("red"), GREEN("green")}

class ColorGrab(val color: Color, val count: Int)

class FullGrab(val allGrabs: List<ColorGrab>) {
}
class Game(val id: Int, val rawGrabList: List<String>) {

    private val grabRegEx = "([0-9]{1,3}) (red|green|blue)".toRegex()
    var allGrabsOfGame : List<FullGrab>

    val maxRed :Int
    val maxGreen: Int
    val maxBlue :Int

    init {
        allGrabsOfGame = rawGrabList.map { constructFullGrab(it) }

        maxBlue = allGrabsOfGame.map { it -> it.allGrabs} .flatMap { e -> e }
            .filter { e -> e.color == Color.BLUE}
            .maxOf { e -> e.count }
        maxRed = allGrabsOfGame.map { it -> it.allGrabs} .flatMap { e -> e }
            .filter { e -> e.color == Color.RED}
            .maxOf { e -> e.count }
        maxGreen = allGrabsOfGame.map { it -> it.allGrabs} .flatMap { e -> e }
            .filter { e -> e.color == Color.GREEN}
            .maxOf { e -> e.count }

    }

    private fun constructFullGrab(singleGrab: String) : FullGrab {
        val colorGrabs = singleGrab.split(",")
            .map { colorGrab -> this.getGrabColorPart(colorGrab) }
            .toMutableList()
        return FullGrab(colorGrabs)
    }

    fun getGrabColorPart(value: String) :ColorGrab {
        return grabRegEx.matchEntire(value.trim())
            ?.destructured
            ?.let { (count, color) ->
                ColorGrab(Color.valueOf(color.uppercase()), count.toInt())
            }!!
    }

    override fun toString(): String {
        return "Game(id=$id, rawGrabList=$rawGrabList, allGrabsOfGame=$allGrabsOfGame, maxRed=$maxRed, maxGreen=$maxGreen, maxBlue=$maxBlue)"
    }
}
class Day02(val input: List<String>) {

    val destructuredRegex = "Game ([0-9]{1,4}): (.*)".toRegex()
    val gameList: List<Game>

    fun getGameFromValue(value: String) :Game {
        return destructuredRegex.matchEntire(value)
            ?.destructured
            ?.let { (gameIdent, grabList) ->
                Game(gameIdent.trim().toInt(), grabList.split(";"))
            }!!
    }
    init {
        gameList = input.map { it -> getGameFromValue(it)}
//        gameList.forEach { println(it)}
    }

    fun getValidGamesForGrab(): List<Game> {
        //12 reds
        return gameList.filter { game -> game.maxRed <= 12 && game.maxGreen <= 13 && game.maxBlue <= 14}
    }

    fun getPowerValueForPart2(): Int {
        //12 reds
        return gameList.map { game -> game.maxRed * game.maxGreen * game.maxBlue} . sum()
    }


}



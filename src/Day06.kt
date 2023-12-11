import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Long {

        var restultPart1: Long = 0
        val part1TestMillis = measureTimeMillis {
            val day06 = Day06(input)
            restultPart1 = day06.getProductOfPossibleWinsOfAllRaces()
            println("restultPart1 " + restultPart1);
        }
        println("part 1 in $part1TestMillis ms")
        return restultPart1
    }

    fun part2(input: List<String>): Long {
        var answer: Long = 0
        val part2TestMillis = measureTimeMillis {
            val day06 = Day06(input)
            answer = day06.getProductOfPossibleWinsOfAllRacesPart2()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "Day06-example"
        "Day06-input"
//        "Day06-pt2-example"
//        "Day06-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

data class Race(val time :Long, val distance :Long) {

}
class Day06(input: List<String>) {

    val raceList: List<Race> = createRaceList(input)
    val singleRacePart2 = createRacePart2(input)

    private fun createRaceList(input: List<String>): List<Race> {
        val raceTimes = input[0].split(":").get(1).split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }

        val raceDistances = input[1].split(":").get(1).split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }

        return raceTimes.zip(raceDistances).map { pair -> Race(pair.first, pair.second) }
    }

    private fun createRacePart2(input: List<String>): Race {
        val raceTime = input[0].split(":").get(1).split(" ")
            .filter { it.isNotEmpty() }
            .joinToString(separator="")
            .toLong()

        val raceDistance = input[1].split(":").get(1).split(" ")
            .filter { it.isNotEmpty() }
            .joinToString(separator="")
            .toLong()

        return Race(raceTime, raceDistance)
    }

    private fun calculateDistanceFor(holdButtonTime: Long, maxTime: Long): Long =  (maxTime - holdButtonTime) * holdButtonTime
    private fun getNumberOfPossibleWins(race: Race) :Long {
        return (1L..race.time).map { calculateDistanceFor(it, race.time) }
                                .filter { it > race.distance }
            .count().toLong()
    }

    fun getProductOfPossibleWinsOfAllRaces() : Long {
        return raceList.map { getNumberOfPossibleWins(it) }
            .reduce(Long::times)
    }

    fun getProductOfPossibleWinsOfAllRacesPart2() : Long {
        return  getNumberOfPossibleWins(singleRacePart2)
    }
}
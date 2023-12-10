import java.text.NumberFormat
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Long {

        var sumOf: Long = 0
        val part1TestMillis = measureTimeMillis {
            val day05 = Day05(input)
            println("minimum location number :  ${day05.calculateLowestDestinationForSeedsAdvanced(day05.seeds.asSequence()).min()}")
//            sumOf = day05.cardList.sumOf { it.score }
//            println("sumOfList " + sumOf);
        }
        println("part 1 in $part1TestMillis ms")
        return sumOf
    }

    fun part2(input: List<String>): Long {
        var answer: Long = 0
        val part2TestMillis = measureTimeMillis {
            val day05 = Day05(input)
            answer = day05.calculateLowestDestinationForSeedRanges()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "day05-example"
        "day05-input"
//        "day05-pt2-example"
//        "day05-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}


class Day05(input: List<String>) {

    val numberFormat = NumberFormat.getNumberInstance(Locale("nl", "NL"))

    val seeds: List<Long> = createSeedList(input.get(0))
    val seedRanges =  createSeedRanges(seeds)

    val seedToSoilMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "seed-to-soil map:")
    val soilToFertilizerMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "soil-to-fertilizer map:")
    val fertilizerToWaterMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "fertilizer-to-water map:")
    val waterToLightMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "water-to-light map:")
    val lightToTemperatureMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "light-to-temperature map:")
    val tempToHumidityMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "temperature-to-humidity map:")
    val humidityToLocationMap : Map<LongRange, (Long) -> Long> = createRangeToFunctionMap(input, "humidity-to-location map:")
    val allMaps: List<Map<LongRange, (Long) -> Long>> = listOf(seedToSoilMap, soilToFertilizerMap, fertilizerToWaterMap, waterToLightMap, lightToTemperatureMap, tempToHumidityMap,  humidityToLocationMap )
    private fun createSeedList(seedsLine: String): List<Long> {
        return seedsLine.split(":").get(1).trim().split(" ")
            .map { it.toLong()}
    }

    private fun createSeedRanges(seeds: List<Long>): List<LongRange> {
        val ranges = seeds.chunked(2).asSequence()
            .map { it[0]..it[0] + it[1] }

        return ranges.toList()
    }

    fun calculateLowestDestinationForSeedRanges(): Long {
        return calculateLowestDestinationForSeedsOptimized(seedRanges)
    }

    /**
     * More advanced solution. Always need a bit more time for using a fold function but saves a lot of code.
     */
    fun calculateLowestDestinationForSeedsAdvanced(allSeeds: Sequence<Long>): Sequence<Long> {
        return allMaps.fold(allSeeds) { accumulatedList, destinationMap ->
            val resultList = accumulatedList.asSequence().map { number ->
                sourceToDestinationTask(number, destinationMap)
            }
            resultList
        }
    }

    /**
     * More memory optimized solution. This iss a bit oldstyled imperative as hell. But the amount of numbers make
     * this something to work better and I dont know yet how to do this in a more elegant way :)
     */
    fun calculateLowestDestinationForSeedsOptimized(allSeedRanges: List<LongRange>): Long {

        var minimum: Long = Long.MAX_VALUE
        println( "All seed count is : ${allSeedRanges.size}")
        var rangeIndex = 0
        allSeedRanges.forEach {
            println("Processing Range $rangeIndex: $it , range Size : ${seedRanges[0].endInclusive - seedRanges[0].start}")
            var count = 1
            for( seedNumber in  it)  {
                var seedToLocation: Long = seedNumber
                for (destinationMap in allMaps) {
                    seedToLocation = sourceToDestinationTask(seedToLocation, destinationMap)
                }
                if (seedToLocation < minimum) {
                    minimum = seedToLocation
                }
                if (count % 25_000_000 == 0) {
                    println("processing count ${numberFormat.format(count)}")
                }
                count++
            }
            rangeIndex++
        }
        return minimum
    }

    private fun sourceToDestinationTask(number: Long, rangeToFunctionMap: Map<LongRange, (Long) -> Long>): Long {
        val functionRange: LongRange? = rangeToFunctionMap.keys.asSequence().find { range -> number in range }
        val soilValue: Long = functionRange.let {
            rangeToFunctionMap.get(it)?.invoke(number)
        } ?: number
        return soilValue
    }

    private fun createRangeToFunctionMap(input: List<String>, mapIdentification: String): Map<LongRange, (Long) -> Long> {

        val linesAfterIdentificationLine = input
            .dropWhile { it != mapIdentification }
            .drop(1) // Skip the header line

           val mapOfRangesToFunctions: Map<LongRange, (Long) -> Long> = linesAfterIdentificationLine
            .takeWhile { it.isNotBlank() }
            .map { line ->
                val (destinationRangeStart, sourceRangeStart, range) = line.split(" ").map(String::toLong)

                var mutationFunction = { i: Long -> i + (destinationRangeStart - sourceRangeStart) }
                val rangeForFunction = sourceRangeStart until (sourceRangeStart + range)

                rangeForFunction to mutationFunction
            }.toMap()

        return mapOfRangesToFunctions
    }
}





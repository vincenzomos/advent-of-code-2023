import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {

        var answer: Int
        val part1TestMillis = measureTimeMillis {
            val day08 = Day08(input)
            answer = day08.getNumberOfStepsToZZZ()
            println("answer part 1 " + answer);
        }
        println("part 1 in $part1TestMillis ms")
        return answer
    }

    fun part2(input: List<String>): BigInteger {
        var answer = BigInteger.valueOf(0L)
        val part2TestMillis = measureTimeMillis {
            val day08 = Day08(input)
            answer = day08.getNumberOfStepsToZForStartPointsA()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "Day08-example"
        "Day08-input"
//        "Day08-pt2-example"
//        "Day08-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

data class MapNode(val id: String, val mapPair: Pair<String, String>) {

    fun getValueForInstruction(direction: Char) :String {
        if (direction == 'L') {
            return mapPair.first
        } else {
            return mapPair.second
        }
    }
}


class Day08(input: List<String>) {

    val regex = """\(([A-Z0-9]{3}), ([A-Z0-9]{3})\)""".toRegex()
    val instructions = input.get(0)
    val mapOfMapNodes: Map<String, MapNode> = createMapOfMapNodes(input)


    private fun createMapOfMapNodes(mapNodeStrings: List<String>): Map<String, MapNode> {

        return mapNodeStrings.drop(2).map { it ->
            val result = it.split("=").map { it.trim() }
            val id = result.get(0)
            val pairString = result.get(1)
            println("id $id  pairString : $pairString")
            val matchResult = regex.find(pairString)!!
            val (leftNode, rightNode) = matchResult.destructured
            id to MapNode(id, Pair(leftNode, rightNode))
        } .toMap()

    }

    fun getNumberOfStepsToZZZ(): Int {

        return generateSequence("AAA" to 0) { (loc, dirIndex) ->
            if (loc == "ZZZ") null
            else {
                val dir = instructions[dirIndex]
                val newDirIndex = if (dirIndex == instructions.length - 1) 0 else dirIndex + 1
                requireNotNull((if (dir == 'L') mapOfMapNodes.get(loc)!!.mapPair.first else mapOfMapNodes[loc]!!.mapPair.second)) to newDirIndex
            }
        }.count() - 1
    }

    fun getNumberOfStepsToZForStartPointsA(): BigInteger {
        val initialLocs = mapOfMapNodes.keys.filter { it.endsWith("A") }
        return findAmountOfStepsForEachSourceAndLcm(initialLocs)
    }

    private fun findAmountOfStepsForEachSourceAndLcm(initialLocs: List<String>): BigInteger {

        val allAmountOfStepsToZ = initialLocs.map {
            var source = it
            var count = 1L
            var dirIndex = 0

            while (true) {
                val direction = instructions[dirIndex]

                val destinationsSource1 = requireNotNull(mapOfMapNodes[source]!!)

                source = destinationsSource1.getValueForInstruction(direction)

                if (source.endsWith("Z")) break

                count++
                dirIndex = if (dirIndex == instructions.length - 1) 0 else dirIndex + 1
            }
            count
        }

        println(allAmountOfStepsToZ)
        val lcm = lcm(allAmountOfStepsToZ.map { it.toBigInteger() })
        println(lcm )
        return lcm
    }

    fun lcm(numbers: List<BigInteger>): BigInteger {
        return numbers.reduce { acc, next ->
            acc.multiply(next).divide(acc.gcd(next))
        }
    }
}





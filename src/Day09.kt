import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {

        var answer: Int
        val part1TestMillis = measureTimeMillis {
            val day09 = Day09(input, Assignment.PART1)
            answer = day09.getAllNewPositions().sum()
            println("answer part 1 " + answer);
        }
        println("part 1 in $part1TestMillis ms")
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer =0
        val part2TestMillis = measureTimeMillis {
            val day09 = Day09(input, Assignment.PART2)
            answer = day09.getAllNewPositions().sum()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "Day09-example"
        "Day09-input"
//        "Day09-pt2-example"
//        "Day09-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

data class InputRange(val initialLine :List<Int>) {

}
class Day09(input: List<String>, val assignment: Assignment ) {

    private var  allNewPositions : List<Int> = processRangeInput(input)

    private fun processRangeInput(input: List<String>) = input.map {
        println("#### Start processing range ############")
        var rangeVariable = it.split("\\s+".toRegex()).map { it.toInt() }.toList()
        rangeVariable.forEach { it -> print("$it  ") }
        kotlin.io.println()
        val lastPositions =  calculateEdgePositionsOfAllLayers(rangeVariable)

        println("Done loop  lastPositions contain $lastPositions")
        val newPositions: MutableList<Int> = constructExtrapolatedPositions(lastPositions)

        val newPositionOfRange = newPositions.last()
        println("Solution new pos range : ${newPositions.last()}")
        println("")
        newPositionOfRange
    }

    private fun calculateEdgePositionsOfAllLayers(
        rangeVariable: List<Int>
    ): MutableList<Int> {
        val edgePositions: MutableList<Int> = mutableListOf()
        var rangeVariable1 = rangeVariable
        while (!rangeVariable1.all { it == 0 }) {
            //store last variable
            edgePositions.add(if (Assignment.PART1 == assignment) rangeVariable1.last() else rangeVariable1.first())
            val differenceList = rangeVariable1.windowed(2).map { it -> it.get(1) - it.get(0) }
            differenceList.forEach { print("$it  ") }
            kotlin.io.println()
            rangeVariable1 = differenceList
        }
        //store also last number in lastPositions. This is always 0
        edgePositions.add(0)
        return edgePositions
    }

    private fun constructExtrapolatedPositions(lastPositions: MutableList<Int>): MutableList<Int> {
        val reversed = lastPositions.reversed()
        val newPositions: MutableList<Int> = mutableListOf()

        reversed.forEachIndexed { index, number ->
            if (index == 0) {
                newPositions.add(number)
            } else {
                if (Assignment.PART1 == assignment) {
                    newPositions.add(number + newPositions[index - 1])
                } else {
                    newPositions.add(number - newPositions[index - 1])
                }
            }
        }
        return newPositions
    }

    fun getAllNewPositions(): List<Int> {
        return allNewPositions
    }
}





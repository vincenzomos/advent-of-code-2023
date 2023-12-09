fun main() {

    fun part1(input: List<String>): Int {
        val day03 = Day03(input)
        val sumOf = day03.getAllNumbersWithAdjacentSymbol().sumOf { it.value }
        println("sumOfList " + sumOf);
        return 1
    }

    fun part2(input: List<String>): Int {
        val day03 = Day03(input)
        return day03.getGearProducts().sum()

    }

// ###############################################################
    val input = readInput(
//        "Day03-example"
        "Day03-input"
//        "Day03-pt2-example"
//        "Day03-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

data class Pos(val row: Int, val col: Int) {
    fun getAdjacentPositions(): Set<Pos> {
        var list = mutableSetOf<Pos>()
        if (row > 0) {
            list.add((Pos(row - 1, col)))
            list.add((Pos(row - 1, col - 1)))
            list.add((Pos(row - 1, col + 1)))
        }
        list.add(Pos(row, col - 1))
        list.add(Pos(row, col + 1))
        list.add(Pos(row + 1, col - 1))
        list.add(Pos(row + 1, col))
        list.add(Pos(row + 1, col + 1))
        return list
    }
}

class Number(val value: Int, val allPositions: List<Pos>) {

    fun getAllAdjacentPositions(): Set<Pos> = allPositions.map { it.getAdjacentPositions() }.flatten().toSet()
    override fun toString(): String {
        return "Number(value=$value, allPositions=$allPositions)"
    }
}

class Day03(input: List<String>) {

    val numberPattern = Regex("\\d+")
    val symbolPattern = Regex("[^\\d.]")
    var gridPointsWithSymbols = mutableSetOf<Pos>()
    var allNumbers = mutableListOf<Number>()
    var gearPosSet = mutableSetOf<Pos>()

    init {
        input.forEachIndexed { rowIndex, line -> processLine(rowIndex, line) }
    }

    private fun processLine(rowIndex: Int, line: String): Unit {
        val matches = numberPattern.findAll(line)

        for (match in matches) {
            val number = match.value.toInt()

            val numberPositions = match.range.map { index -> Pos(rowIndex, index) }.toList()
            allNumbers.add(Number(number, numberPositions))
        }

        val symbolMatches = symbolPattern.findAll(line)
        for (match in symbolMatches) {
            val symbol = match.value
            val symbolPositions = match.range.map { index -> Pos(rowIndex, index) }.toList()
            symbolPositions.forEach(gridPointsWithSymbols::add)
            if ("*" == symbol) {
                gearPosSet.add(Pos(rowIndex, match.range.first))
            }
        }
    }

    fun getAllNumbersWithAdjacentSymbol(): List<Number> {

        return allNumbers.filter { it -> it.getAllAdjacentPositions().any(gridPointsWithSymbols::contains) }
    }

    fun getGearProducts(): List<Int> {
        val posNumberMap: Map<Pos, Number> = allNumbers
            .flatMap { number -> number.allPositions.map { position -> position to number } }
            .toMap()

        val gearProducts: List<Int> = gearPosSet.mapNotNull { gear ->
            val allAdjacentNumbers = gear.getAdjacentPositions().mapNotNull { posNumberMap[it] }.toSet()
            if (allAdjacentNumbers.size == 2) {
                allAdjacentNumbers
                    .map { it.value }
                    .fold(1) { acc, element -> acc * element }
            } else {
                null
            }
        }

        return gearProducts
    }
}





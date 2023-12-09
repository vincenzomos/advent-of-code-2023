import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Int {

        var sumOf: Int
        val part1TestMillis = measureTimeMillis {
            val day04 = Day04(input)
            sumOf = day04.cardList.sumOf { it.score }
            println("sumOfList " + sumOf);
        }
        println("part 1 in $part1TestMillis ms")
        return sumOf
    }

    fun part2(input: List<String>): Int {
        var answer: Int
        val part2TestMillis = measureTimeMillis {
            val day04 = Day04(input)
            answer = day04.getNumberOfScratchCards()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "Day04-example"
        "Day04-input"
//        "Day04-pt2-example"
//        "Day04-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

data class Card(val winningNumbers: List<Int>, val myNumbers: List<Int>, val score: Int, val copyWins: Int)
class Day04(input: List<String>) {

    val cardList: List<Card> = createCardList(input)

    private fun createCardList(input: List<String>): List<Card> {
        return input.map { it ->
                val (winnersString, myNumbersString) = it.split(":").get(1).trim().split("|")
                val winnerList = winnersString.trim().split(" ").mapNotNull { it ->
                   if (it.isNotBlank()) it.trim().toInt() else null }.toList()
                val myNumberList = myNumbersString.split(" ").mapNotNull { it ->
                    if (it.isNotBlank()) it.trim().toInt() else null }.toList()
                val myWinningNumbers = myNumberList.filter { winnerList.contains(it) }
                val score = calculateCardScore(myWinningNumbers.size)
                Card(winnerList, myNumberList, score, myWinningNumbers.size)
            }
        }

    private fun calculateCardScore(size: Int): Int {
        return when(size) {
            0 -> 0
            1 -> 1
            else -> 2.0.pow(size-1).toInt()
        }
    }

    fun getNumberOfScratchCards(): Int {
        val sumOfCopyWins = cardList.mapIndexed { index, it -> getAmountOfCardsForIndex(index) } .sum()
        return sumOfCopyWins
    }

    private fun getAmountOfCardsForIndex(index: Int) :Int {
        if (cardList[index].copyWins == 0) {
            return 1 //represents just the original card
        } else {
            var sumOfOriginalsAndCopyWins = 1  //initialized to 1 for the original card
            for (i in 1..cardList[index].copyWins) {
                sumOfOriginalsAndCopyWins += getAmountOfCardsForIndex(index+i)
            }
            return sumOfOriginalsAndCopyWins
        }
    }

}





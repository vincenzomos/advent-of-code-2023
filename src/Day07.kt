import kotlin.system.measureTimeMillis

fun main() {

    fun part1(input: List<String>): Long {

        var restultPart1: Long
        val part1TestMillis = measureTimeMillis {
            val useJokerInGame = false
            val day07 = Day07(input, useJokerInGame)
            restultPart1 = day07.getScoreForCompleteList()
            println("restultPart1 " + restultPart1);
        }
        println("part 1 in $part1TestMillis ms")
        return restultPart1.toLong()
    }

    fun part2(input: List<String>): Long {
        var answer: Long = 0
        val part2TestMillis = measureTimeMillis {
            val useJokerInGame = true
            val day07 = Day07(input, useJokerInGame)

            answer = day07.getScoreForCompleteList()
        }
        println("part 2 in $part2TestMillis ms")
        return answer
    }

// ###############################################################
    val input = readInput(
//        "Day07-example"
//        "Day07-input"
//        "Day07-pt2-example"
        "Day07-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

val cardOrder = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cardOrderLowToHigh = cardOrder.reversed()
val cardOrderWithJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
val cardOrderWithJokerLowToHigh = cardOrderWithJoker.reversed()
var upgradedCards :MutableList<String> = mutableListOf()

enum class HandType(val handValue: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_AKIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1);
}


data class Hand(val handInput: String, val useJokerInGame: Boolean) : Comparable<Hand> {

    //Added String value to see HandUpgrade process
    var upgradedCard: String = ""
    val cards = handInput.split(" ").get(0)
    val score = handInput.split(" ").get(1).toInt()
    val distinctCharsCount = cards.toSet().size
    val charMapToCount: Map<Char, Int> = cards.groupingBy { it }.eachCount()
    val handType: HandType = determineHandType(cards)


    private fun getHandTypeFor2DistinctCards(cards: String): HandType {
        if (charMapToCount.values.containsAll(listOf(2, 3))) {
            //if one of the cards with 2 is J it can be upgraded to 5 Of Kind
            if (useJokerInGame && cards.contains('J')) {
                upgradedCard = "$cards orig : ${HandType.FULL_HOUSE} to ${HandType.FIVE_OF_A_KIND}"
                return HandType.FIVE_OF_A_KIND
            }
            return HandType.FULL_HOUSE
        } else { // This situation is for situation there are 2 cards with counts of occurences (1,4)
                 // If J is one of these chars the hand can be upgraded to Five Of a Kind
            if(useJokerInGame && charMapToCount.containsKey('J')) {
                upgradedCard = "$cards orig : ${HandType.FOUR_OF_A_KIND} to ${HandType.FIVE_OF_A_KIND}"
                return HandType.FIVE_OF_A_KIND
            }
            return HandType.FOUR_OF_A_KIND
        }
    }

    private fun getHandTypeFor3DistinctCards(cards: String): HandType {

        if (charMapToCount.values.containsAll(listOf(1, 1, 3))) {
            //possible upgrade to four of a kind for both situations if J is the one with count 3 or just a count of 1
            if (useJokerInGame && charMapToCount.containsKey('J')) {
                upgradedCard = "$cards orig : ${HandType.THREE_OF_AKIND} to ${HandType.FOUR_OF_A_KIND}"
                return HandType.FOUR_OF_A_KIND
            }
            return HandType.THREE_OF_AKIND
        } else {
            // this means a list of counts per card of (2,2,1)
            // If J has a count of 2 it can be upgraded to 4 of a kind. If it is only once it can be upgraded to Full-house
            if (useJokerInGame && charMapToCount.filter { c -> c.value == 2 }.containsKey('J')) {
                upgradedCard = "$cards orig : ${HandType.TWO_PAIR} to ${HandType.FOUR_OF_A_KIND}"
                return HandType.FOUR_OF_A_KIND
            }
            if (useJokerInGame && charMapToCount.filter { c -> c.value == 1 }.containsKey('J')) {
                upgradedCard = "$cards orig : ${HandType.TWO_PAIR} to ${HandType.FULL_HOUSE}"
                return HandType.FULL_HOUSE
            }
            return HandType.TWO_PAIR
        }
    }

    private fun determineHandType(cards: String): HandType {
//        println("In determineHandType for cards $cards")
        val initialHandType = when (distinctCharsCount) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> getHandTypeFor2DistinctCards(cards)
            3 -> getHandTypeFor3DistinctCards(cards)
            4 -> getHandTypeFor4DistinctCards(cards)
            else -> getHandTypeFor5DistinctCards(cards)
        }
        return initialHandType
    }

    private fun getHandTypeFor5DistinctCards(cards: String): HandType {
      //If hand contains 'J' it can be upgraded to ONE_PAIR
      if (useJokerInGame && cards.contains('J')) {
          upgradedCard = "$cards orig : ${HandType.HIGH_CARD} to ${HandType.ONE_PAIR}"
          return HandType.ONE_PAIR
      } else {
          return HandType.HIGH_CARD
      }
    }

    private fun getHandTypeFor4DistinctCards(cards: String): HandType {
        // the counts of cards will look like this (1,1,1,2)
        // As long as J is in the cards there can be an upgrade to THREE_OF_AKIND
        if (useJokerInGame && charMapToCount.containsKey('J')) {
            upgradedCard = "$cards orig : ${HandType.ONE_PAIR} to ${HandType.THREE_OF_AKIND}"
            return HandType.THREE_OF_AKIND
        }
        return HandType.ONE_PAIR
    }

    override fun compareTo(other: Hand): Int {
        val compareToOnHandType = this.handType.handValue.compareTo(other.handType.handValue)
        if (compareToOnHandType != 0) {
            return compareToOnHandType
        } else {
            val zippedCards = cards.zip(other.cards)
            val cardOrderForGame = if (useJokerInGame) cardOrderWithJokerLowToHigh else cardOrderLowToHigh
            for (pair in zippedCards) {
                val thisCardValue = cardOrderForGame.indexOf(pair.first)
                val otherCardValue = cardOrderForGame.indexOf(pair.second)
//                println("In detailed compare for $cards with ${other.cards}")
                if (thisCardValue.compareTo(otherCardValue) != 0) {
                    return thisCardValue.compareTo(otherCardValue)
                }
            }
        }
        return 0 //This should never be triggered
    }
}

class Day07(input: List<String>, val useJokerInGame: Boolean) {

    val handList: List<Hand> = input.map { Hand(it, useJokerInGame) }

    fun getScoreForCompleteList(): Long {
        val sorted = handList.sorted()



        val zippedScoreWithRank = sorted.map { it }.zip(sorted.mapIndexed { index, hand -> index + 1 })
        val sumTotal = zippedScoreWithRank.map { it -> it.first.score.toLong() * it.second.toLong() }.sum()

        sorted.filter { it -> it.upgradedCard != "" } .forEach { it -> println(it.upgradedCard)}

//       DebugLogging to find what hands with J I missed
//        println("#####################   MISS ANYTHING #################")
//        sorted.filter { it -> it.upgradedCard == "" && it.cards.contains('J') } .forEach { it -> println(it.cards)}
        return sumTotal
    }
}
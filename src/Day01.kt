enum class DigitEnum(val value: String, val digit: Char) {
    ONE("one", '1'),
    TWO("two", '2'),
    THREE("three", '3'),
    FOUR("four", '4'),
    FIVE("five", '5'),
    SIX("six", '6'),
    SEVEN("seven", '7'),
    EIGHT("eight", '8'),
    NINE("nine", '9');

    fun intValue(): Int {
        return digit.digitToInt()
    }
}
class IndexContainingDigit(val index: Int, val digitEnum: DigitEnum)

fun extractAllIndexesContainingStringDigits(rawValue: String, digitEnum: DigitEnum): List<IndexContainingDigit> {
    val indices = Regex(digitEnum.value.toString()).findAll(rawValue)
        .map { indexes ->  IndexContainingDigit(indexes.range.first, digitEnum) }

        .toList()

    return indices
}
fun extractAllIndexesContainingDigits(rawValue: String, digitEnum: DigitEnum): List<IndexContainingDigit> {
    val indices = Regex(digitEnum.digit.toString()).findAll(rawValue)
        .map { indexes ->  IndexContainingDigit(indexes.range.first, digitEnum) }
        .toList()
    return indices
}


fun getCalibrationValue(rawValue: String) :Int {
    val mapForNumericDigits = DigitEnum.values().map { it -> extractAllIndexesContainingDigits(rawValue, it) }
        .flatMap { it }.groupBy { it -> it.index }

    val mapForStringDigits = DigitEnum.values().map { it -> extractAllIndexesContainingStringDigits(rawValue, it) }
        .flatMap { it }.groupBy { it -> it.index }

    //union maps
    val totalMap = mapForNumericDigits + mapForStringDigits
    val minimalDigit  = totalMap.get(totalMap.minOf { it -> it.key})!!.first().digitEnum
    val maxDigit  = totalMap.get(totalMap.maxOf { it -> it.key})!!.first().digitEnum

    return "${minimalDigit.digit}${maxDigit.digit}".toInt()

}

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { it.filter { c -> c.isDigit()}}
            .also(::println)
            .map { "${it[0]}${it[it.length-1]}".toInt() }
            .sum()
    }


    fun part2(input: List<String>): Int {
        return input.map { getCalibrationValue(it) }
            .sum()
    }

// ###############################################################
    val input = readInput(
//        "Day01-example"
//        "Day01-input"
//        "Day01-pt2-example"
        "Day01-pt2-input"
    )
    println("#### Part 1 result : ")
//    part1(input).println()
    println("#### Part 2 result : ")
    part2(input).println()
}

import java.io.BufferedReader
import java.io.FileReader
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines


/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun readCompleteFileIntoString(name:String) :String {
    return FileReader("src/main/resources/$name.txt").readText()
}

enum class Assignment {PART1, PART2}
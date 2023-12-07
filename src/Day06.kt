import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.streams.asStream
import kotlin.text.Typography.times

fun main() {
    fun part1(input: List<String>): Int {
        val times = input[0].parseSpaceSeparatedInts()
        val distances = input[1].parseSpaceSeparatedInts()

        return times.zip(distances) { time, distance ->
            solve(time.toLong(), distance.toLong())
        }.map { range ->
            range.count()
        }.fold(1) { current, next ->
            current * next
        }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].parseSpaceSeparatedInts().joinToString("") { it.toString() }.toLong()
        val distance = input[1].parseSpaceSeparatedInts().joinToString("") { it.toString() }.toLong()

        return solve(time, distance).count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}


private fun solve(time: Long, distance: Long): IntRange {
    val minTime = (-time + sqrt(time.toDouble().pow(2) - (4.0 * (distance + 1)))) / -2
    val maxTime = (-time - sqrt(time.toDouble().pow(2) - (4.0 * (distance + 1)))) / -2
    return ceil(minTime).roundToInt() .. floor(maxTime).roundToInt()
}
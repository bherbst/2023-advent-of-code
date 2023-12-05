import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val matchCount = it.countMatches().toInt()
            val score = if (matchCount > 0) 2.0.pow(matchCount - 1) else 0.0
            score.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cardScores = input.mapIndexed { index, line ->
            Pair(index, line.countMatches().toInt())
        }.toMap()
        val copies = cardScores.mapValues { 1 }.toMutableMap()
        cardScores.forEach{ index, matchCount ->
            if (matchCount == 0) return@forEach
            val start = min(input.size, index + 1)
            val end = min(input.size, index + matchCount)
            (start .. end).forEach { copyIndex ->
                copies[copyIndex]  = copies[copyIndex]!! + copies[index]!!
            }
        }
        
        return copies.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private val cardRegex = Regex("Card\\s+\\d+: (.*) \\| (.*)")
private fun String.countMatches(): Int {
    val (_, winningString, numsString) = cardRegex.find(this)!!.groupValues
    val winningNumbers = winningString.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    val cardNumbers = numsString.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    return cardNumbers.count { winningNumbers.contains(it) }
}
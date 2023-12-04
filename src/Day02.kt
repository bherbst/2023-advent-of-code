import java.awt.Color.blue
import java.awt.Color.green
import java.awt.Color.red

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.getGameResults() }
            .filter { (_, results) ->
                results.all { it.isGamePossible(12, 13, 14) }
            }
            .sumOf { (gameNum, _) -> gameNum }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.getGameResults() }
            .map { (_, results) ->
                val (red, green, blue) = results.minColorsRequired()

                // "power" of cubes
                red * green * blue
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private val lineRegex = Regex("Game (\\d+): (.*)")

private fun String.getGameResults(): Pair<Int, List<String>> {
    val groups = lineRegex.find(this)!!.groupValues
    val gameNum = groups[1].toInt()
    val results = groups[2].split("; ")
    return Pair(gameNum, results)
}

private fun String.isGamePossible(red: Int, green: Int, blue: Int): Boolean {
    return split(", ").all {
        val (countString, color) = it.split(" ")
        when (color) {
            "red" -> countString.toInt() <= red
            "green" -> countString.toInt() <= green
            "blue" -> countString.toInt() <= blue
            else -> false
        }
    }
}

private fun String.colorCounts(): Triple<Int, Int, Int> {
    val colorCounts = split(", ").map {
        val (countString, color) = it.split(" ")
        Pair(color, countString)
    }.toMap()

    return Triple(
        colorCounts.get("red")?.toInt() ?: 0,
        colorCounts.get("green")?.toInt() ?: 0,
        colorCounts.get("blue")?.toInt() ?: 0
    )
}

private fun List<String>.minColorsRequired(): Triple<Int, Int, Int> {
    val colorCounts = map { it.colorCounts() }
    val red = colorCounts.maxOf { it.first }
    val green = colorCounts.maxOf { it.second }
    val blue = colorCounts.maxOf { it.third }
    return Triple(red, green, blue)
}
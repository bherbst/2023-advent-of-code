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
        val leftRight = input.first()
        val nodes = input.drop(2).toNodes()

        var currentNode = "AAA"
        var steps = 0
        var currentIndex = 0
        while (currentNode != "ZZZ") {
            steps++
            val nextDir = leftRight[currentIndex]
            val currentMap = nodes.get(currentNode)
            currentNode = if (nextDir == 'L') currentMap!!.first else currentMap!!.second
            currentIndex = if (currentIndex == leftRight.length - 1) 0 else currentIndex + 1
        }

        return steps
    }

    fun part2(input: List<String>): Int {
        val leftRight = input.first()
        val nodes = input.drop(2).toNodes()

        val currentNodes = nodes.keys.filter { it.endsWith("A") }.toMutableList()
        var steps = 0
        var currentIndex = 0
        while (currentNodes.any { !it.endsWith("Z") }) {
            steps++
            currentNodes.forEachIndexed { index, node ->
                val nextDir = leftRight[currentIndex]
                val currentMap = nodes.get(node)
                currentNodes[index] = if (nextDir == 'L') currentMap!!.first else currentMap!!.second
            }
            currentIndex = if (currentIndex == leftRight.length - 1) 0 else currentIndex + 1
        }

        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}


private val nodeRegex = Regex("(\\w){3}")

private fun List<String>.toNodes(): Map<String, Pair<String, String>> {
    val nodeMap = mutableMapOf<String, Pair<String, String>>()
    forEach { line ->
        val (node, left, right) = nodeRegex.findAll(line).map { it.groups.first() }.filterNotNull().map { it.value }.toList()
        nodeMap.put(node, Pair(left, right))
    }
    return nodeMap
}
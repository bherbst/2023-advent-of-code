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
    fun part1(input: List<String>): Long {
        return input.map { it.parseHand() }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid.toLong() }
            .sum()
    }

    fun part2(input: List<String>): Long {
        return input.map { it.parseHand(true) }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid.toLong() }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput) == 5905L)

    println("========")
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private val cardLabels = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
private val cardLabels2 = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
private enum class HandType(val strength: Int) {
    FiveOfKind(7),
    FourOfKind(6),
    FullHouse(5),
    ThreeOfKind(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1)
}

private data class Day7Hand(
    val cards: String,
    val bid: Int,
    val isPart2: Boolean = false
) : Comparable<Day7Hand> {
    val handType = cards.getHandType(isPart2)

    override fun compareTo(other: Day7Hand): Int {
        val labelsList = if (isPart2) cardLabels2 else cardLabels
        if (handType == other.handType) {
            cards.forEachIndexed { index, label ->
                val otherLabel = other.cards[index]
                if (label != otherLabel) {
                    return labelsList.indexOf(label).compareTo(labelsList.indexOf(otherLabel))
                }
            }
        } else {
            return handType.strength.compareTo(other.handType.strength)
        }

        return 0
    }
}

private fun String.parseHand(isPart2: Boolean = false): Day7Hand {
    val (cards, bid) = split(" ").filter { it.isNotBlank() }
    return Day7Hand(
        cards = cards,
        bid = bid.toInt(),
        isPart2 = isPart2
    )
}

private fun String.getHandType(optimize: Boolean): HandType {
    val groups = groupBy { it }
    val handType = when (groups.size) {
        1 -> HandType.FiveOfKind
        5 -> HandType.HighCard
        else -> {
            val groupSizes = groups.map { (_, cards) -> cards.size }
            if (groupSizes.contains(4)) {
                HandType.FourOfKind
            } else if (groupSizes.contains(3)) { //at least one triplet
                if (groupSizes.contains(2)) {
                    HandType.FullHouse
                } else {
                    HandType.ThreeOfKind
                }
            } else { // at least one pair
                val numPairs = groupSizes.count { it == 2 }
                if (numPairs == 2) {
                    HandType.TwoPair
                } else {
                    HandType.OnePair
                }
            }
        }

    }

    if (!optimize) {
        return handType
    } else {
        if (this.all { it == 'J' }) return HandType.FiveOfKind
        val (maxChar, _) = groups.filter { it.key != 'J' }.maxBy { it.value.count() }
        val bestHand = replace('J', maxChar)
        return bestHand.getHandType(false)
    }
}
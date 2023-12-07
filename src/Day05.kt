import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.streams.asStream

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().extractSeeds()
        val maps = input.getThingMaps()

        val locations = seeds.map { seed -> maps.locationForSeed(seed) }

        return locations.min()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input.first().extractSeedRanges()
        val maps = input.getThingMaps()

        return seedRanges.minOf { range ->
            range.asSequence().minOf { seed -> maps.locationForSeed(seed) }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private data class ThingMapEntry(
    val destStart: Long,
    val sourceStart: Long,
    val length: Long
) {
    val inputRange = sourceStart..sourceStart + (length - 1)
    fun mapValue(input: Long): Long {
        // assumes we are in range
        return input - (sourceStart - destStart)
    }
}

private class Day5Input(
    val seeds: List<Long>,
    val maps: List<List<ThingMapEntry>>
)

private fun String.extractSeeds(): List<Long> {
    return substring(7).parseSpaceSeparatedNumbers()
}

private fun String.extractSeedRanges(): List<LongRange> {
    val numbers = substring(7).parseSpaceSeparatedNumbers()
    return numbers.windowed(size = 2, step = 2).map { (start, length) ->
        LongRange(start, start + length - 1)
    }
}

private fun String.parseSpaceSeparatedNumbers(): List<Long> {
    return split(" ").mapNotNull { it.toLongOrNull() }
}

private fun List<String>.getThingMaps(): List<List<ThingMapEntry>> {
    val maps = mutableListOf<List<ThingMapEntry>>()
    var currentMap = mutableListOf<ThingMapEntry>()
    drop(2).forEach { line ->
        if (line.isBlank()) {
            maps.add(currentMap)
            currentMap = mutableListOf()
        } else if (!line.any { it.isDigit() }) {
            // skip - this is a header e.g. "soil-to-fertilizer map"
        } else {
            val (destStart, sourceStart, length) = line.parseSpaceSeparatedNumbers()
            currentMap += ThingMapEntry(destStart, sourceStart, length)
        }
    }
    maps.add(currentMap)
    return maps
}

private fun List<ThingMapEntry>.mapValue(value: Long): Long {
    val entry = firstOrNull { it.inputRange.contains(value) }
    return entry?.let { it.mapValue(value) } ?: value
}

private fun List<List<ThingMapEntry>>.locationForSeed(seed: Long): Long {
    return fold(seed) { id, map ->
        map.mapValue(id)
    }
}
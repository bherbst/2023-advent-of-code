fun main() {
    fun part1(input: List<String>): Int {
        return input.getAllParts().sumOf { it.value }
    }

    fun part2(input: List<String>): Long {
        var gearMap = mutableMapOf<GearId, List<PartNumber>>()
        input.getAllParts()
            .forEach { part ->
                part.gearIds.forEach { id -> gearMap.addPart(id, part) }
            }


        val gearList =  gearMap.filter { (_, parts) -> parts.size == 2 }
            .toList()
        var sum = 0L
        gearMap.filter { (_, parts) -> parts.size == 2 }
            .toList()
            .forEach { (_, parts) -> sum += parts[0].value * parts[1].value }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun MutableMap<GearId, List<PartNumber>>.addPart(id: GearId, part: PartNumber) {
    put(id, getOrDefault(id, emptyList()) + part)
}

private data class FoundNumber(val value: Int, val start: Int) {
    private val length = value.toString().length
    val end = start + length
}

private data class GearId(val x: Int, val y: Int)

private data class PartNumber(val value: Int, val gearIds: List<GearId>)

private val digitsRegex = Regex("(\\d+)")

private fun List<String>.getAllParts(): List<PartNumber> {
    var index = 0
    var allParts = windowed(size = 3, step = 1)
        .map { lines ->
            ++index
            getPartNumbers(line = lines[1], previousLine = lines[0], nextLine = lines[2], index)
        }
        .flatten()
    // Add first and last special cases
    allParts += getPartNumbers(line = first(), previousLine = null, nextLine = get(1), lineIndex = 0)
    allParts += getPartNumbers(line = last(), previousLine = get(size - 2), nextLine = null, lineIndex = size - 1)
    return allParts
}

private fun String.getNumbers(): List<FoundNumber> {
    val numbers = digitsRegex.findAll(this)
        .map { it.groups.drop(1) }
        .map { it.first() }
        .filterNotNull()
    return numbers.mapNotNull {
        FoundNumber(value = it.value.toInt(), start = it.range.start)
    }.toList()
}

private fun getPartNumbers(line: String, previousLine: String?, nextLine: String?, lineIndex: Int): List<PartNumber> {
    val numbers = line.getNumbers()
    return numbers.filter { isPartNumber(it, line, previousLine, nextLine) }
        .map { PartNumber(it.value, getGearIds(it, line, previousLine, nextLine, lineIndex)) }
}

private fun isPartNumber(number: FoundNumber, line: String, previousLine: String?, nextLine: String?): Boolean {
    if (number.start > 0 && line.get(number.start - 1).isSymbol()) return true
    if (number.end < (line.length - 1) && line.get(number.end).isSymbol()) return true

    return (previousLine?.containsAdjacentToRange(number.start, number.end) ?: false
      || nextLine?.containsAdjacentToRange(number.start, number.end) ?: false
    )
}

private fun getGearIds(number: FoundNumber, line: String, previousLine: String?, nextLine: String?, lineIndex: Int): List<GearId> {
    var gears = mutableListOf<GearId>()
    if (number.start > 0 && line.get(number.start - 1).isGear())
        gears.add(GearId(0, lineIndex))
    if (number.end < (line.length - 1) && line.get(number.end).isGear())
        gears.add(GearId(number.end, lineIndex))

    val adjacentMin = maxOf(number.start - 1, 0)
    val adjacentMax = minOf(number.end, line.length - 1)
    val range = adjacentMin..adjacentMax
    gears += previousLine?.mapIndexedNotNull { index, value ->
        if (index in range && value.isGear()) {
            GearId(index, lineIndex - 1)
        } else null
    } ?: emptyList()
    gears += nextLine?.mapIndexedNotNull { index, value ->
        if (index in range && value.isGear()) {
            GearId(index, lineIndex + 1)
        } else null
    } ?: emptyList()

    return gears
}

private fun Char.isSymbol(): Boolean {
    return !isLetterOrDigit() && !isWhitespace() && this != '.'
}
private fun Char.isGear(): Boolean {
    return this == '*'
}

private fun String.containsAdjacentToRange(start: Int, end: Int): Boolean {
    val min = maxOf(start - 1, 0)
    val max = minOf(end, length - 1)
    return substring(min..max).any { it.isSymbol() }
}

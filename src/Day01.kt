fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { it.calibrationValue() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { it.calibrationValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}


private fun String.calibrationValue(): Int {
    val digits = this
        .replace("zero", "z0o")
        .replace("one", "o1e")
        .replace("two", "t2o")
        .replace("three", "t3e")
        .replace("four", "f4r")
        .replace("five", "f5e")
        .replace("six", "s6x")
        .replace("seven", "s7n")
        .replace("eight", "e8t")
        .replace("nine", "n9e")
        .filter { it.isDigit() }
    println("$this => $digits")
    return "${digits.first()}${digits.last()}".toInt()
}
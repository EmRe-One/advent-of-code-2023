package tr.emreone.adventofcode.days

object Day01 {

    private val spelledDigits = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )

    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                val first = line.first { c -> c.isDigit() }
                val last = line.last { c -> c.isDigit() }

                (first.toString() + last.toString()).toInt()
            }
            .sumOf { it }
    }

    fun part2(input: List<String>): Int {
        val pattern = "(?=(one|two|three|four|five|six|seven|eight|nine|\\d))".toRegex()

        val mappedInput = input
            .map { line ->
                val matches = pattern.findAll(line).map {
                    val value = it.groups[1]?.value
                    value?.let { v -> spelledDigits[v] ?: v.toInt() }
                }.toList()

                val first = matches.first()!!.toInt()
                val last = matches.last()!!.toInt()

                first * 10 + last
            }

        return mappedInput.sumOf { it }
    }

}

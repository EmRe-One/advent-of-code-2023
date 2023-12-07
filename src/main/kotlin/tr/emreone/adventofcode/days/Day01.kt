package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day01: Day(
    1,
    2023,
    "Trebuchet?!",
    session= Resources.resourceAsString("session.cookie")
) {

    private val spelledDigits = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
    )

    override fun part1(): Any {
        return inputAsList
            .sumOf { line ->
                val first = line.first { c -> c.isDigit() }
                val last = line.last { c -> c.isDigit() }

                "$first$last".toInt()
            }
    }

    override fun part2(): Any {
        val pattern = "(?=(one|two|three|four|five|six|seven|eight|nine|\\d))".toRegex()

        val mappedInput = inputAsList
            .map { line ->
                val matches = pattern.findAll(line).map {
                    val value = it.groups[1]?.value
                    value?.let { v -> spelledDigits[v] ?: v.toInt() }
                }.toList()

                val first = matches.first()!!
                val last = matches.last()!!
                
                first * 10 + last
            }

        return mappedInput.sumOf { it }
    }
}

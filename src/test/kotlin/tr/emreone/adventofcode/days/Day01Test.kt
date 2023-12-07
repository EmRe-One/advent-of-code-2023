package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {
    solve<Day01>(false) {
        Resources.resourceAsList("day01_1_example.txt")
            .joinToString("\n") part1 142

        Resources.resourceAsList("day01_2_example.txt")
            .joinToString("\n") part2 281
    }
}

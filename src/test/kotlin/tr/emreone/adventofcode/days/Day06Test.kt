package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {
    solve<Day06>(true) {
        Resources.resourceAsList("day06_example.txt")
            .joinToString("\n") part1 288 part2 71503L
    }
}

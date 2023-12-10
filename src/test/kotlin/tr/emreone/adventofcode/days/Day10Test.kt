package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {
    solve<Day10>(true) {
        Resources.resourceAsList("day10_example.txt")
            .joinToString("\n") part1 8
    }
}

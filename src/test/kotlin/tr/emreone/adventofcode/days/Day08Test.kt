package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {
    solve<Day08>(false) {
        Resources.resourceAsList("day08_1_example.txt")
            .joinToString("\n") part1 6
    }

    solve<Day08>(false) {
        Resources.resourceAsList("day08_2_example.txt")
            .joinToString("\n") part2 6
    }
}

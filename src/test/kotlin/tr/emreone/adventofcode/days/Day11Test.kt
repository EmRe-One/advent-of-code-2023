package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day11>(true) {
        Resources.resourceAsList("day11_example.txt")
            .joinToString("\n") part1 374 part2 82_000_210L
    }

}

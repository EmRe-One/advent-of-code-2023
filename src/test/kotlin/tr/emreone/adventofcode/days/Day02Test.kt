package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {
    solve<Day02>(false) {
        Resources.resourceAsList("day02_example.txt")
            .joinToString("\n") part1 8 part2 2286
    }
}

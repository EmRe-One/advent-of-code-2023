package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve
import kotlin.test.Test

internal class Day05Test {

    @Test
    fun `execute_tests`() {
        solve<Day05>(false) {
            Resources.resourceAsList("day05_example.txt")
                .joinToString("\n") part1 35L part2 46L
        }
    }

}

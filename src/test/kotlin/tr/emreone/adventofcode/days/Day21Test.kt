package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day21Test {

    @Test
    fun `execute_tests`() {
        solve<Day21>(false) {
            Resources.resourceAsList("day21_example.txt")
                .joinToString("\n") part1 16 part2 167033
        }
    }

}

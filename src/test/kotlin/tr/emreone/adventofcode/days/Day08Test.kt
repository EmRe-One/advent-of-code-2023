package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day08Test {

    @Test
    fun `execute_tests`() {
        solve<Day08>(false) {
            Resources.resourceAsList("day08_1_example.txt")
                .joinToString("\n") part1 6
        }

        solve<Day08>(false) {
            Resources.resourceAsList("day08_2_example.txt")
                .joinToString("\n") part2 6
        }
    }

}

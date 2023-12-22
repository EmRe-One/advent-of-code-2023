package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day25Test {

    @Test
    fun `execute_tests`() {
        solve<Day25>(false) {
            Resources.resourceAsList("day25_example.txt")
                .joinToString("\n") part1 0
        }
    }

}

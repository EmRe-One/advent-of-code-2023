package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day24Test {

    @Test
    fun `execute_tests`() {
        solve<Day24>(false) {
            Resources.resourceAsList("day24_example.txt")
                .joinToString("\n") part1 0
        }
    }

}

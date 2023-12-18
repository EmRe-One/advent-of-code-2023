package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day16Test {

    @Test
    fun `execute_tests`() {
        solve<Day16>(false) {
            Resources.resourceAsList("day16_example.txt")
                .joinToString("\n") part1 46
        }
    }

}

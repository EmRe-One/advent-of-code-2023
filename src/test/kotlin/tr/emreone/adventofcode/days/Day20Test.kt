package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day20Test {

    @Test
    fun `execute_tests`() {
        solve<Day20>(false) {
            Resources.resourceAsList("day20_example.txt")
                .joinToString("\n") part1 32_000_000
        }
    }

}

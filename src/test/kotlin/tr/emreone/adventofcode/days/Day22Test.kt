package tr.emreone.adventofcode.days

import org.junit.jupiter.api.Test
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

internal class Day22Test {

    @Test
    fun `execute_tests`() {
        solve<Day22>(false) {
            Resources.resourceAsList("day22_example.txt")
                .joinToString("\n") part1 5 part2 7
        }
    }

}

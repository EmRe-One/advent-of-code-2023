package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day06 : Day(
    6,
    2023,
    "Wait For It",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Any? {
        val values = inputAsList.map {line ->
            line.split(":")[1]
                .trim()
                .split("\\s".toRegex())
        }


        return super.part1()
    }

    override fun part2(): Any? {
        return super.part2()
    }

}

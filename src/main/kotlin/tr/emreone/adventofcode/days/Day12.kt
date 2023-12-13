package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

class Day12 : Day(12, 2023, "Hot Springs") {

    class SpringCondition(row: String) {
        val springConditions: String
        val criteria: List<Int>

        init {
            this.springConditions = row.split(" ")[0]
            this.criteria = row.split(" ")[1].split(",").map { it.toInt() }
        }

        fun numberOfPossibleArrangements(currentIndex: Int, criteria: List<Int>): Int {
            return 0
        }
    }

    override fun part1(): Int {
        return 0
    }

    override fun part2(): Int {
        return 0
    }

}

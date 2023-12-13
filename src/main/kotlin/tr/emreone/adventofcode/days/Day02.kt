package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

class Day02 : Day(2, 2023, "Cube Conundrum") {

    class Bag(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0
    ) {
        val MAX_RED = 13
        val MAX_GREEN = 14
        val MAX_BLUE = 15

        fun possible(): Boolean {
            return this.red <= MAX_RED
                    && this.green <= MAX_GREEN
                    && this.blue <= MAX_BLUE;
        }
    }

    private val GAME_PATTERN = "Game (?<id>\\d+)".toRegex()
    private val BAG_PATTERN = "(?<number>\\d+) (?<color>red|green|blue)".toRegex()

    private fun getBags(input: List<String>): Map<Int, List<Bag>> {
        return input.associate { line ->
            val (first, second) = line.split(":")

            val id = GAME_PATTERN.matchEntire(first)!!.groups["id"]!!.value.toInt()

            val bags = second.split(";").map { bag ->
                val groups = BAG_PATTERN.findAll(bag)
                    .associate {
                        it.groups["color"]!!.value to it.groups["number"]!!.value.toInt()
                    }

                Bag(
                    groups["red"] ?: 0,
                    groups["green"] ?: 0,
                    groups["blue"] ?: 0
                )
            }

            id to bags
        }
    }

    override fun part1(): Int {
        return getBags(inputAsList)
            .filterValues {
                it.all { b -> b.possible() }
            }
            .keys
            .sum()
    }

    override fun part2(): Int {
        return getBags(inputAsList)
            .map { (_, bags) ->
                val minRed = bags.maxOf { it.red }
                val minGreen = bags.maxOf { it.green }
                val minBlue = bags.maxOf { it.blue }

                minRed * minGreen * minBlue
            }
            .sum()
    }
}

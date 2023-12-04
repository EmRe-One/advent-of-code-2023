package tr.emreone.adventofcode.days

object Day02 {

    class Bag(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0
    ) {

        fun possible(maxRed: Int, maxGreen: Int, maxBlue: Int): Boolean {
            return this.red <= maxRed
                    && this.green <= maxGreen
                    && this.blue <= maxBlue;
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

    fun part1(input: List<String>, maxRed: Int = 0, maxGreen: Int = 0, maxBlue: Int = 0): Int {
        return getBags(input)
            .filterValues {
                it.all { b -> b.possible(maxRed, maxGreen, maxBlue) }
            }
            .keys
            .sum()
    }

    fun part2(input: List<String>): Int {
        return getBags(input)
            .map { (id, bags) ->
                val minRed = bags.maxOf { it.red }
                val minGreen = bags.maxOf { it.green }
                val minBlue = bags.maxOf { it.blue }

                minRed * minGreen * minBlue
            }
            .sum()
    }
}

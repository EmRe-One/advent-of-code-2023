package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day06 : Day(
    6,
    2023,
    "Wait For It",
    session = Resources.resourceAsString("session.cookie")
) {

    class Race(val time: Long, val distance: Long) {

        /**
         *
         */
        fun getClickDurationsForWin(): List<Pair<Long, Long>> {
            val results = mutableListOf<Pair<Long, Long>>()

            for (t in 1 .. this.time/2) {
                val clickLength = t
                val speed = t
                val remainingTime = this.time - t

                val possibleDistance = speed * remainingTime
                if (possibleDistance > this.distance) {
                    results.add(t to possibleDistance)
                    if (t != remainingTime) {
                        results.add(remainingTime to possibleDistance)
                    }
                }
            }

            return results
        }
    }

    override fun part1(): Any? {
        val lineNumbers = inputAsList.map { line ->
            line.split(":")[1]
                .trim()
                .split("\\s+".toRegex())
        }
        val races = lineNumbers[0].zip(lineNumbers[1]).map {
            Race(it.first.toLong(), it.second.toLong())
        }

        return races
            .map {
                it.getClickDurationsForWin().size
            }
            .reduce { acc, i ->
                acc * i
            }
    }

    override fun part2(): Any? {
        val lineNumbers = inputAsList.map {
            it.split(":")[1]
                .replace("\\s+".toRegex(), "")
                .toLong()
        }

        val race = Race(lineNumbers[0], lineNumbers[1])

        return race.getClickDurationsForWin().size
    }

}

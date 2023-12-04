package tr.emreone.adventofcode

import tr.emreone.adventofcode.days.*
import tr.emreone.utils.Logger.logger
import tr.emreone.utils.Resources
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
class Solutions {

    /**
     * day is the day nummer of the puzzle.
     *
     * part is the part of the puzzle.
     * - 01 = 1 -> for only part 1
     * - 10 = 2 -> for only part 2 or
     * - 11 = 3 -> for both day parts
     */
    fun solveDay(day: Int, part: Int = 3) {
        val dayString = day.toString().padStart(2, '0')
        val dayClass = Class.forName("tr.emreone.adventofcode.days.Day$dayString")

        if (part.and(0b01) > 0) {
            val part1 = dayClass.getMethod("part1", List::class.java)
            val input = Resources.resourceAsList("day${dayString}.txt")
            val (result, duration) = measureTimedValue { part1.invoke(null, input) }

            logger.info("Day$dayString, Part1 solve in $duration:")
            logger.info { result  }
        }
        if (part.and(0b10) > 0) {
            val part2 = dayClass.getMethod("part2", List::class.java)
            val input = Resources.resourceAsList("day${dayString}.txt")
            val (result, duration) = measureTimedValue { part2.invoke(null, input) }

            logger.info("Day$dayString, Part2 solve in $duration:")
            logger.info { result  }
        }
    }
    fun solveDay01() {
        val input = Resources.resourceAsList(fileName = "day01.txt")

        val (part1, elapsedTime1) = measureTimedValue {
            Day01.part1(input)
        }
        logger.info { "Part1 solved in $elapsedTime1:" }
        logger.info { part1 }

        val (part2, elapsedTime2) = measureTimedValue {
            Day01.part2(input)
        }
        logger.info { "Part2 solved in $elapsedTime2:" }
        logger.info { part2 }
    }
// $1

}

fun main() {
    val solution = Solutions()
    val day = 1
    val dayString = day.toString().padStart(2, '0')
    logger.info { "Solving Puzzles for Day $dayString: " }

    try {
        val currentDay = solution.javaClass.getMethod("solveDay$dayString")
        currentDay.invoke(solution)
    } catch (e: Exception) {
        e.printStackTrace()
        logger.error { "Day $dayString is not implemented yet!" }
    }
}

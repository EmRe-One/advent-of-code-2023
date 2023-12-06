package tr.emreone.adventofcode

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import org.reflections.Reflections
import tr.emreone.adventofcode.days.*
import tr.emreone.kotlin_utils.Logger.logger
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import kotlin.time.Duration
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
    fun solveDay02() {
        val input = Resources.resourceAsList(fileName = "day02.txt")

        val (part1, elapsedTime1) = measureTimedValue {
            Day02.part1(input, 12, 13, 14)
        }
        logger.info { "Part1 solved in $elapsedTime1:" }
        logger.info { part1 }

        val (part2, elapsedTime2) = measureTimedValue {
            Day02.part2(input)
        }
        logger.info { "Part2 solved in $elapsedTime2:" }
        logger.info { part2 }
    }
    fun solveDay03() {
        val input = Resources.resourceAsList(fileName = "day03.txt")

        val (part1, elapsedTime1) = measureTimedValue {
            Day03.part1(input)
        }
        logger.info { "Part1 solved in $elapsedTime1:" }
        logger.info { part1 }

        val (part2, elapsedTime2) = measureTimedValue {
            Day03.part2(input)
        }
        logger.info { "Part2 solved in $elapsedTime2:" }
        logger.info { part2 }
    }
    fun solveDay04() {
        val input = Resources.resourceAsList(fileName = "day04.txt")

        val (part1, elapsedTime1) = measureTimedValue {
            Day04.part1(input)
        }
        logger.info { "Part1 solved in $elapsedTime1:" }
        logger.info { part1 }

        val (part2, elapsedTime2) = measureTimedValue {
            Day04.part2(input)
        }
        logger.info { "Part2 solved in $elapsedTime2:" }
        logger.info { part2 }
    }
    fun solveDay05() {
        val input = Resources.resourceAsText(fileName = "day05.txt")

        val (part1, elapsedTime1) = measureTimedValue {
            Day05.part1(input)
        }
        logger.info { "Part1 solved in $elapsedTime1:" }
        logger.info { part1 }

        val (part2, elapsedTime2) = measureTimedValue {
            Day05.part2(input)
        }
        logger.info { "Part2 solved in $elapsedTime2:" }
        logger.info { part2 }
    }
// $1

}

private fun getAllDayClasses(): Collection<Class<out Day>> =
    Reflections("").getSubTypesOf(Day::class.java).filter { it.simpleName.matches(Regex("Day\\d+")) }

fun main() {
    val solution = Solutions()
    val day = 5
    val dayString = day.toString().padStart(2, '0')
    logger.info { "Solving Puzzles for Day $dayString: " }

    try {
        val currentDay = solution.javaClass.getMethod("solveDay$dayString")
        currentDay.invoke(solution)
    } catch (e: Exception) {
        e.printStackTrace()
        logger.error { "Day $dayString is not implemented yet!" }
    }


    val aocTerminal = Terminal()

    with(aocTerminal) {
        println(com.github.ajalt.mordant.rendering.TextColors.red("\n~~~ Advent Of Code Runner ~~~\n"))
        val dayClasses = getAllDayClasses().sortedBy(::dayNumber)
        val totalDuration = dayClasses
            .map { it.execute() }
            .reduceOrNull(kotlin.time.Duration::plus)

        println("\nTotal runtime: ${com.github.ajalt.mordant.rendering.TextColors.red("$totalDuration")}")
    }
}

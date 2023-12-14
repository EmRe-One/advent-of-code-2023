package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.lcm

class Day08 : Day(8, 2023, "Haunted Wasteland") {

    private fun parseInput(): Pair<List<Int>, Map<String, List<String>>> {
        val command = inputAsList[0].toCharArray().map { if (it == 'L') 0 else 1 }
        val maps = inputAsList.drop(2).associate {
            val (key, value) = it.split("=")

            val destinations = value
                .trim()
                .trimStart('(')
                .trimEnd(')')
                .split(',')
                .map {
                    it.trim()
                }

            key.trim() to destinations
        }

        return command to maps
    }

    override fun part1(): Int {
        val (command, maps) = parseInput()
        var currentPosition = "AAA"

        var counter = 0
        val totalCommands = command.size
        while (currentPosition != "ZZZ") {
            val index = (counter.mod(totalCommands))
            // print("%6d: $currentPosition to ($index -> ${ if (command[index] == 0) 'L' else 'R'}) ".format(counter))
            currentPosition = maps[currentPosition]?.get(command[index]) ?: throw IllegalStateException()
            // println(currentPosition)
            counter++
        }

        return counter
    }

    override fun part2(): Long {
        val (command, maps) = parseInput()
        val startPositions = maps.keys.filter { it.endsWith('A') }.toMutableList()

        val totalCommands = command.size

        val steps = startPositions.map {
            var currentPosition = it

            var counter = 0L
            while (!currentPosition.endsWith('Z')) {
                val index = (counter.mod(totalCommands))
                currentPosition = maps[currentPosition]?.get(command[index]) ?: throw IllegalStateException()
                counter++
            }

            counter
        }

        return steps.lcm()
    }

}

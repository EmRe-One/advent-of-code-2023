package tr.emreone.adventofcode.days

import tr.emreone.adventofcode.days.Sequence
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

typealias Sequence = MutableList<Long>

class Day09 : Day(
    9,
    2023,
    "Mirage Maintenance",
    session = Resources.resourceAsString("session.cookie")
) {

    private fun predictNext(sequence: Sequence): Long {
        val differences = mutableListOf<Sequence>()

        differences.add(sequence.windowed(2).map { it[1] - it[0] }.toMutableList())

        while(differences.last().any { it != 0L }) {
            val lastDifferences = differences.last()
            differences.add(lastDifferences.windowed(2).map { it[1] - it[0] }.toMutableList())
        }

        differences.last().add(0)
        differences.reversed().windowed(2).map {
            it[1].add(it[1].last() + it[0].last())
        }

        sequence.add(sequence.last() + differences[0].last())

        /*
        println(sequence.joinToString("  "))
        differences.forEachIndexed { index, seq ->
            println("  ".repeat(index + 1) + seq.joinToString("  "))
        }
        */

        return sequence.last()
    }

    private fun predictPrevious(sequence: Sequence): Long {
        val differences = mutableListOf<Sequence>()

        differences.add(sequence.windowed(2).map { it[1] - it[0] }.toMutableList())

        while(differences.last().any { it != 0L }) {
            val lastDifferences = differences.last()
            differences.add(lastDifferences.windowed(2).map { it[1] - it[0] }.toMutableList())
        }

        differences.last().add(0, 0)
        differences.reversed().windowed(2).map {
            it[1].add(0, it[1].first() - it[0].first())
        }

        sequence.add(0, sequence.first() - differences[0].first())

        /*
        println(sequence.joinToString("  "))
        differences.forEachIndexed { index, seq ->
            println("  ".repeat(index + 1) + seq.joinToString("  "))
        }
        */

        return sequence.first()
    }

    override fun part1(): Long {
        return inputAsList.sumOf { line ->
            predictNext(line.split("\\s+".toRegex()).map { it.toLong() }.toMutableList())
        }
    }

    override fun part2(): Long {
        return inputAsList.sumOf { line ->
            predictPrevious(line.split("\\s+".toRegex()).map { it.toLong() }.toMutableList())
        }
    }

}

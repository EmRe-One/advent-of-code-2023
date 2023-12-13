package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import kotlin.math.pow

class Day04 : Day(4, 2023, "Scratchcards") {

    private fun parseMatches(input: List<String>): Map<Int, Int> {
        return input.associate { line ->
            val (card, game) = line.split(":")
            val cardId = card.split("\\s+".toRegex())[1].toInt()
            val (winningNumbers, yourNumbers) = game.split("|")

            val winningNumberList = winningNumbers.trim().split("\\s+".toRegex())
            val yourNumberList = yourNumbers.trim().split("\\s+".toRegex())

            val matches = yourNumberList.count { it in winningNumberList }

            cardId to matches
        }
    }

    override fun part1(): Int {
        return parseMatches(inputAsList)
            .map { (_, matches) ->
                if (matches == 0) {
                    0
                } else {
                    2.0.pow(matches.toDouble() - 1).toInt()
                }
            }
            .sum()
    }

    override fun part2(): Int {
        val cards = parseMatches(inputAsList)

        val scratchcards = MutableList(cards.size) {
            0
        }
        cards.forEach { (id, score) ->
            val currentIndex = id - 1
            scratchcards[currentIndex]++

            for (i in 1..score) {
                if (currentIndex + i in scratchcards.indices) {
                    scratchcards[currentIndex + i] += scratchcards[currentIndex]
                }
            }
        }
        return scratchcards.sum()
    }

}

package tr.emreone.adventofcode.days

import kotlin.math.pow

object Day04 {

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

    fun part1(input: List<String>): Int {
        return parseMatches(input)
            .map { (id, matches) ->
                if (matches == 0) {
                    0
                } else {
                    2.0.pow(matches.toDouble() - 1).toInt()
                }
            }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cards = parseMatches(input)

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

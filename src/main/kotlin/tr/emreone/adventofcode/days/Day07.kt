package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.times

class Day07 : Day(7, 2023, "Camel Cards") {

    class Hand(val hand: String, val value: Int, val withJoker: Boolean = false) : Comparable<Hand> {
        val CARDS = buildList<String> {
            if (withJoker) {
                add("A,K,Q,T,9,8,7,6,5,4,3,2,J")
            } else {
                add("A,K,Q,J,T,9,8,7,6,5,4,3,2")
            }
        }.first()
            .split(",")
            .map { it.toCharArray()[0] }
            .reversed()

        fun rank(): Int {
            val sortedCardSet = if (withJoker && this.hand.contains("J")) {
                val jokerNumber = hand.count { it == 'J' }

                val charList = hand.toCharArray().filter { it != 'J' }
                    .groupBy { it }
                    .toMutableMap()

                if (charList.isEmpty()) {
                    charList['A'] = "AAAAA".toCharArray().toList()
                } else {
                    val maxUsedChar = charList.values.maxBy { it.size }[0]

                    charList[maxUsedChar] = charList[maxUsedChar]!!.toMutableList().apply {
                        addAll("$maxUsedChar".times(jokerNumber).toCharArray().toList())
                    }
                }

                charList.entries
                    .sortedBy {
                        it.value.size
                    }
            } else {
                hand.toCharArray()
                    .groupBy { it }
                    .entries
                    .sortedBy {
                        it.value.size
                    }
            }

            return if (sortedCardSet.size == 1) {
                6 // "Five of a kind"
            } else if (sortedCardSet.size == 2) {
                if (sortedCardSet[0].value.size == 1) {
                    5 // "Four of a kind"
                } else {
                    4 // "Full House"
                }
            } else if (sortedCardSet.size == 3) {
                if (sortedCardSet[2].value.size == 3) {
                    3 // "Three of a kind"
                } else {
                    2 // "Two Pair"
                }
            } else if (sortedCardSet.size == 4) {
                1 // "One Pair"
            } else {
                0 // "High Card"
            }
        }

        override fun compareTo(other: Hand): Int {
            if (this.rank() < other.rank()) return -1
            if (this.rank() > other.rank()) return 1

            for (i in 0 until this.hand.length) {
                if (this.hand[i] != other.hand[i]) {
                    return CARDS.indexOf(this.hand[i]) - CARDS.indexOf(other.hand[i])
                }
            }
            throw IllegalStateException("Should be not possible")
        }

        override fun toString(): String {
            return "$hand $value"
        }
    }

    override fun part1(): Long {
        val sortedValues = inputAsList
            .map {
                val (hand, value) = it.split(" ")
                Hand(hand, value.toInt())
            }
            .sorted()

        return sortedValues
            .mapIndexed { index, hand ->
                hand.value.toLong() * (index + 1)
            }
            .sum()
    }

    override fun part2(): Long {
        val sortedValues = inputAsList
            .map {
                val (hand, value) = it.split(" ")
                Hand(hand, value.toInt(), true)
            }
            .sorted()

        return sortedValues
            .mapIndexed { index, hand ->
                hand.value.toLong() * (index + 1)
            }
            .sum()
    }

}

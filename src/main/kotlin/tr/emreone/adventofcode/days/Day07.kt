package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day07 : Day(
    7,
    2023,
    "Camel Cards",
    session = Resources.resourceAsString("session.cookie")
) {

    class Hand(val hand: String, val value: Int): Comparable<Hand> {
        val CARDS = "A,K,Q,J,T,9,8,7,6,5,4,3,2"
            .split(",")
            .map { it.toCharArray()[0] }
            .reversed()

        val setOfCards = hand.toCharArray().groupBy { it }
        val adjustedHand = replaceJoker()

        fun rank(): Int {
            val sortedCardSet = setOfCards.entries.sortedBy {
                it.value.size
            }
            return if (sortedCardSet.size == 1) {
                6 // "Five of a kind"
            }
            else if (sortedCardSet.size == 2) {
                if (sortedCardSet[0].value.size == 1) {
                    5 // "Four of a kind"
                }
                else {
                    4 // "Full House"
                }
            }
            else if (sortedCardSet.size == 3) {
                if (sortedCardSet[2].value.size == 3) {
                    3 // "Three of a kind"
                }
                else {
                    2 // "Two Pair"
                }
            } else if (sortedCardSet.size == 4) {
                1 // "One Pair"
            }
            else {
                0 // "High Card"
            }
        }

        fun replaceJoker(): String {
            if (this.hand.contains("J")) {
                val maxChar = this.hand.toCharArray()
                    .filter { it != 'J' }
                    .groupBy { it }
                    .maxBy { it.value.size }
                    .key
                
            }
            else {
                return ""
            }
        }

        override fun compareTo(other: Hand): Int {
            if (this.rank() < other.rank()) return -1
            if (this.rank() > other.rank()) return 1

            for(i in 0 until this.hand.length) {
                if (this.hand[i] != other.hand[i]){
                    return CARDS.indexOf(this.hand[i]) - CARDS.indexOf(other.hand[i])
                }
            }
            throw IllegalStateException("Should be not possible")
        }

        fun compareToWithJoker(other: Hand): Int {
            if (this.rank() < other.rank()) return -1
            if (this.rank() > other.rank()) return 1

            for(i in 0 until this.hand.length) {
                if (this.hand[i] != other.hand[i]){
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
                Hand(it.split(" ")[0], it.split(" ")[1].toInt())
            }
            .sorted()

        return sortedValues
            .mapIndexed { index, hand ->
                hand.value.toLong() * (index + 1)
            }
            .sum()
    }

    override fun part2(): Any? {
        val sortedValues = inputAsList
            .map {
                Hand(it.split(" ")[0], it.split(" ")[1].toInt())
            }
            .sorted()

        return sortedValues
            .mapIndexed { index, hand ->
                hand.value.toLong() * (index + 1)
            }
            .sum()
    }

}

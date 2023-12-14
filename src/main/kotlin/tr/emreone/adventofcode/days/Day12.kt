package tr.emreone.adventofcode.days

import tr.emreone.adventofcode.CacheSupport.withCaching
import tr.emreone.kotlin_utils.automation.Day

class Day12 : Day(12, 2023, "Hot Springs") {

    data class State(
        val charIndex: Int,
        val lastGroupIndex: Int,
        val lastGroupSize: Int,
        val isGroupActive: Boolean
    ) {
        fun nextState(inGroup: Boolean): State {
            return copy(
                charIndex = charIndex + 1,
                lastGroupIndex = if (!isGroupActive && inGroup) lastGroupIndex + 1 else lastGroupIndex,
                lastGroupSize = if (!isGroupActive && inGroup) 1 else if (inGroup) lastGroupSize + 1 else lastGroupSize,
                isGroupActive = inGroup
            )
        }
    }

    class SpringCondition(row: String, factor: Int = 1) {
        val spring: String
        val groups: List<Int>

        companion object {
            const val UNKNOWN_STATE = '?'
            const val WORKKING_STATE = '#'
            const val EMPTY = '.'
        }

        init {
            val (a, b) = row.split(" ").take(2)
            val springList = mutableListOf<String>()
            val groupList = mutableListOf<Int>()

            if (factor > 0) {
                for (i in 0 until factor) {
                    springList.add(a)
                    groupList.addAll(b.split(",").map { it.toInt() })
                }
            }

            this.spring = springList.joinToString("?")
            this.groups = groupList.toList()
        }

        fun possibleArrangements(): Long {
            return process(
                state = State(
                    charIndex = 0,
                    lastGroupIndex = -1,
                    lastGroupSize = 0,
                    isGroupActive = false
                )
            )
        }

        private fun process(state: State): Long = withCaching(state) {
            if (it.lastGroupIndex >= this.groups.size) {
                0L
            }
            else if (!it.isGroupActive && it.lastGroupSize < this.groups.getOrElse(it.lastGroupIndex, { 0 })) {
                0L
            }
            else if (it.lastGroupSize > this.groups.getOrElse(it.lastGroupIndex, { 0 })) {
                0L
            }
            else if (it.charIndex == this.spring.length) {
                // Check for completeness
                if (it.lastGroupSize == this.groups.getOrElse(it.lastGroupIndex, { 0 })
                    && it.lastGroupIndex == this.groups.size - 1
                ) {
                    1L
                }
                else {
                    0L
                }
            }
            else {
                var total = 0L
                when (this.spring[it.charIndex]) {
                    UNKNOWN_STATE  -> {
                        // Wildcard, process both options
                        total += process(state = it.nextState(inGroup = false))
                        total += process(state = it.nextState(inGroup = true))
                    }

                    EMPTY          -> {
                        // Finish group if possible
                        total += process(state = it.nextState(inGroup = false))
                    }

                    WORKKING_STATE -> {
                        // Increase group count
                        total += process(state = it.nextState(inGroup = true))
                    }
                }
                total
            }
        }
    }

    override fun part1(): Long {
        return inputAsList.sumOf {
            SpringCondition(it).possibleArrangements()
        }
    }

    override fun part2(): Long {
        return inputAsList.sumOf {
            SpringCondition(it, 5).possibleArrangements()
        }
    }

}

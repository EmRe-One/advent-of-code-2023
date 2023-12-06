package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Logger.logger

object Day05 {

    class Process {

        private var steps =
            arrayOf("seed", "soil", "fertilizer", "water", "light", "temperature", "humidity", "location")

        private var currentStepNumber = 0
        val currentStep: String
            get() = this.steps[this.currentStepNumber]

        fun next(): String? {
            if (this.currentStepNumber == this.steps.size - 1) {
                return null
            }
            return this.steps[this.currentStepNumber + 1]
        }

        fun stepForward() {
            if (this.next() != null) {
                this.currentStepNumber++
            }
        }
    }

    class Mapping(val srcStart: Long, private val destStart: Long, private val length: Long) {
        private val offset: Long = this.destStart - this.srcStart

        fun inRange(x: Long): Boolean {
            return x in this.srcStart until (this.srcStart + length)
        }

        fun mapNumber(x: Long): Long {
            if (this.inRange(x))
                return x + this.offset;

            return x
        }
    }

    fun part1(input: String): Long {
        val blocks = input.split("(\r?\n){2}".toRegex())

        val seeds = blocks[0].split(":")[1].trim().split("\\s".toRegex()).map { it.toLong() }
        val maps = blocks.drop(1).associate { m ->
            val lines = m.split("(\r?\n){1}".toRegex())
            val title = lines[0].split("\\s".toRegex())[0]
            val list = lines.drop(1)
                .map {
                    val (dest, src, length) = it.split("\\s".toRegex())

                    Mapping(src.toLong(), dest.toLong(), length.toLong())
                }
                .sortedBy {
                    it.srcStart
                }

            title to list
        }

        return seeds
            .minOf { seed ->
                val process = Process()
                var currentNumber = seed
                logger.debug { "Seed: $currentNumber " }

                do {
                    val title = "${process.currentStep}-to-${process.next()}"
                    logger.debug { "-> " + process.next() + ": " }

                    currentNumber = maps[title]
                        ?.firstOrNull() {
                            it.inRange(currentNumber)
                        }
                        ?.mapNumber(currentNumber)
                        ?: currentNumber

                    logger.debug { "$currentNumber " }
                    process.stepForward()
                } while (process.next() != null)

                println()
                currentNumber
            }
    }

    fun part2(input: String): Long {
        val blocks = input.split("(\r?\n){2}".toRegex())

        val seedRanges = blocks[0]
            .split(":")[1]
            .trim()
            .split("\\s".toRegex())
            .map { it.toLong() }
            .windowed(2, 2)

        val maps = blocks.drop(1).associate { m ->
            val lines = m.split("(\r?\n){1}".toRegex())
            val title = lines[0].split("\\s".toRegex())[0]
            val list = lines.drop(1)
                .map {
                    val (dest, src, length) = it.split("\\s".toRegex())

                    Mapping(src.toLong(), dest.toLong(), length.toLong())
                }
                .sortedBy {
                    it.srcStart
                }

            title to list
        }

        var min = Long.MAX_VALUE

        seedRanges.forEach {
            for (seed in it[0] until it[0] + it[1]) {
                val process = Process()
                var currentNumber = seed

                do {
                    val title = "${process.currentStep}-to-${process.next()}"

                    currentNumber = maps[title]
                        ?.firstOrNull() {
                            it.inRange(currentNumber)
                        }
                        ?.mapNumber(currentNumber)
                        ?: currentNumber

                    process.stepForward()
                } while (process.next() != null)

                min = minOf(min, currentNumber)
            }
        }
        return min
    }
}

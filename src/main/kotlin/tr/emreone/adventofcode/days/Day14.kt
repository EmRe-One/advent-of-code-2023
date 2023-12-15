package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Coords
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.plus

class Day14 : Day(14, 2023, "Parabolic Reflector Dish") {

    class Platform(input: List<String>) {
        val grid = input.map {
            it.toMutableList()
        }.toMutableList()
        private val height = this.grid.size
        private val width = this.grid.get(0).size

        companion object {
            const val ROUNDED_ROCK = 'O'
            const val CUBED_ROCK = '#'
            const val EMPTY = '.'
        }

        private fun _move(x: Int, y: Int, direction: Direction4) {
            if (this.grid[y][x] == EMPTY || this.grid[y][x] == CUBED_ROCK) {
                return
            }

            var position = Coords(x, y)
            var next: Coords

            while (true) {
                next = position.plus(direction)
                if (next.first !in this.grid.get(0).indices
                    || next.second !in this.grid.indices
                ) {
                    break
                }
                if (this.grid[next.second][next.first] != EMPTY) {
                    break
                }

                this.grid[next.second][next.first] = ROUNDED_ROCK
                this.grid[position.second][position.first] = EMPTY
                position = next
            }
        }

        fun tiltPlatform(direction: Direction4) {
            when (direction) {
                Direction4.NORTH -> {
                    for (xi in 0 until width) {
                        for (yi in 0 until height) {
                            this._move(xi, yi, direction)
                        }
                    }
                }

                Direction4.EAST  -> {
                    for (yi in 0 until height) {
                        for (xi in (width - 1) downTo 0) {
                            this._move(xi, yi, direction)
                        }
                    }
                }

                Direction4.SOUTH -> {
                    for (xi in 0 until width) {
                        for (yi in (height - 1) downTo 0) {
                            this._move(xi, yi, direction)
                        }
                    }
                }

                Direction4.WEST  -> {
                    for (yi in 0 until height) {
                        for (xi in 0 until width) {
                            this._move(xi, yi, direction)
                        }
                    }
                }
            }
        }

        fun cycle() {
            this.tiltPlatform(Direction4.NORTH)
            this.tiltPlatform(Direction4.WEST)
            this.tiltPlatform(Direction4.SOUTH)
            this.tiltPlatform(Direction4.EAST)
        }

        fun totalLoad(): Long {
            return this.grid.mapIndexed { index, line ->
                line.map {
                    if (it == ROUNDED_ROCK) {
                        (height - index).toLong()
                    }
                    else {
                        0L
                    }
                }.sum()
            }.sum()
        }

        fun getState(): String {
            return this.grid.joinToString("\n") { line ->
                line.joinToString("")
            }
        }

        fun print() {
            println(this.getState())
        }

    }

    override fun part1(): Long {
        val platform = Platform(inputAsList)
        platform.tiltPlatform(Direction4.NORTH)
        return platform.totalLoad()
    }

    override fun part2(): Long {
        val platform = Platform(inputAsList)

        var prevCounter = 0L
        var counter = 0L
        val stateMap = mutableMapOf(platform.getState() to 0L)

        // find a loop instead of iterating 1_000_000_000
        while (true) {
            platform.cycle()
            counter++
            if (stateMap.containsKey(platform.getState())) {
                prevCounter = stateMap.get(platform.getState())!!
                break
            }
            if (counter == 1_000_000_000L) {
                break
            }
            stateMap[platform.getState()] = counter
        }

        val remainingCycles = (1_000_000_000L - counter).mod(counter - prevCounter)
        repeat(remainingCycles.toInt()) {
            platform.cycle()
        }
        return platform.totalLoad()
    }
}

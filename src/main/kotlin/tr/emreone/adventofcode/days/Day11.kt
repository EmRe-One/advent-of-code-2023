package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.manhattanDistanceTo
import tr.emreone.kotlin_utils.math.Coords

class Day11 : Day(
    11,
    2023,
    "Cosmic Expansion",
    session = Resources.resourceAsString("session.cookie")
) {

    class Galaxy(x: Int, y: Int) {
        var coord = Coords(x, y)

        fun distanceTo(other: Galaxy): Int {
            return coord.manhattanDistanceTo(other.coord)
        }
    }

    class Universe(input: List<List<Char>>) {
        val GALAXY_CHAR = '#'
        val map = input.mapIndexed { y, lines ->
            lines.mapIndexedNotNull { x, c ->
                if (c == GALAXY_CHAR) {
                    Galaxy(x, y)
                } else {
                    null
                }
            }
        }.flatten()

        var width = this.map.maxOf { it.coord.first } + 1
        var height = this.map.maxOf { it.coord.second } + 1

        fun expand(factor: Int = 2) {
            val emptyColumns = (0 until width).mapNotNull { x ->
                val numberOfGalaxiesInColumn = this.map.count { it.coord.first == x }

                if (numberOfGalaxiesInColumn == 0) {
                    x
                } else {
                    null
                }
            }
            val emptyRows = (0 until height).mapNotNull { y ->
                val numberOfGalaxiesInRow = this.map.count { it.coord.second == y }
                if (numberOfGalaxiesInRow == 0) {
                    y
                } else {
                    null
                }
            }

            this.map.forEach { g ->
                val diffX = emptyColumns.count { it < g.coord.first }
                val diffY = emptyRows.count { it < g.coord.second }

                g.coord = Coords(g.coord.first + diffX * (factor - 1), g.coord.second + diffY * (factor - 1))
            }
            this.width += emptyColumns.size * (factor - 1)
            this.height += emptyRows.size * (factor - 1)
        }

        fun print() {
            (0 until height).forEach { y ->
                (0 until width).forEach { x ->
                    this.map.firstOrNull { it.coord.first == x && it.coord.second == y }
                        ?.also {
                            print('#')
                        }
                        ?: print('.')
                }
                println()
            }
        }
    }

    override fun part1(): Int {
        val universe = Universe(inputAsGrid)
        universe.expand()

        return universe.map.sumOf {a ->
            universe.map.sumOf { b ->
                a.distanceTo(b)
            }
        } / 2
    }

    override fun part2(): Long {
        val universe = Universe(inputAsGrid)
        universe.expand(1_000_000)

        return universe.map.sumOf {a ->
            universe.map.sumOf { b ->
                a.distanceTo(b).toLong()
            }
        } / 2
    }

}
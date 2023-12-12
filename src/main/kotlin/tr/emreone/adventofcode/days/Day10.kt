package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.*

class Day10 : Day(
    10,
    2023,
    "Pipe Maze",
    session = Resources.resourceAsString("session.cookie")
) {

    class Maze(input: List<List<Char>>) {
        val NORTH_EAST = '└'  // oder 'L'
        val NORTH_WEST = '┘'  // oder 'J'
        val SOUTH_EAST = '┌'  // oder 'F'
        val SOUTH_WEST = '┐'  // oder '7'
        val NORTH_SOUTH = '│' // oder '|'
        val WEST_EAST = '─'   // oder '-'
        val EMPTY_CELL_CHAR = '∙' // '.'
        val FILLED_CELL_CHAR = '#'

        private val PIPES_TO_NORTH = listOf(NORTH_SOUTH, NORTH_WEST, NORTH_EAST)

        val width = input.first().size
        val height = input.size
        val grid = input.mapIndexed { y, line ->
            line.mapIndexed { x, p ->
                Pipe(x, y, convertChar(p))
            }
        }

        /**
         * convert the char for a better visualization
         */
        private fun convertChar(c: Char): Char {
            return when (c) {
                'L'  -> NORTH_EAST
                'J'  -> NORTH_WEST
                '|'  -> NORTH_SOUTH
                'F'  -> SOUTH_EAST
                '7'  -> SOUTH_WEST
                '-'  -> WEST_EAST
                'S'  -> 'S'
                else -> EMPTY_CELL_CHAR
            }
        }

        val allowedPipesInDirection: Map<Char, List<Char>> = mapOf(
            'N' to listOf(NORTH_SOUTH, SOUTH_WEST, SOUTH_EAST, 'S'),
            'E' to listOf(WEST_EAST, NORTH_WEST, SOUTH_WEST, 'S'),
            'S' to listOf(NORTH_SOUTH, NORTH_WEST, NORTH_EAST, 'S'),
            'W' to listOf(WEST_EAST, SOUTH_EAST, NORTH_EAST, 'S')
        )

        private fun getNeighbours(pipe: Pipe): Map<Char, Pipe> {
            return buildMap {
                if (pipe.y > 0) {
                    this['N'] = this@Maze.grid[pipe.y - 1][pipe.x]
                }
                if (pipe.x < this@Maze.width - 1) {
                    this['E'] = this@Maze.grid[pipe.y][pipe.x + 1]
                }
                if (pipe.y < this@Maze.height - 1) {
                    this['S'] = this@Maze.grid[pipe.y + 1][pipe.x]
                }
                if (pipe.x > 0) {
                    this['W'] = this@Maze.grid[pipe.y][pipe.x - 1]
                }
            }
        }

        fun getValidNeighbours(pipe: Pipe): List<Pipe> {
            val neighboursCompass = getNeighbours(pipe).filter {
                it.value.pipeChar != EMPTY_CELL_CHAR
            }
            val lookAtDirection = when (pipe.pipeChar) {
                'S'         -> listOf('N', 'E', 'S', 'W')
                NORTH_SOUTH -> listOf('N', 'S')
                WEST_EAST   -> listOf('W', 'E')
                NORTH_EAST  -> listOf('N', 'E')
                NORTH_WEST  -> listOf('N', 'W')
                SOUTH_WEST  -> listOf('S', 'W')
                SOUTH_EAST  -> listOf('S', 'E')
                else        -> throw IllegalArgumentException()
            }
            return neighboursCompass.mapNotNull {
                if (it.key in lookAtDirection && it.value.pipeChar in allowedPipesInDirection[it.key]!!) {
                    it.value
                }
                else {
                    null
                }
            }
        }

        fun getLoop(startPipe: Pipe): List<Pipe>? {
            val stack = ArrayDeque<Triple<Pipe, List<Pipe>, List<Pipe>>>()
            stack.add(Triple(startPipe, emptyList(), emptyList()))

            while (stack.isNotEmpty()) {
                val (currentPipe, visited, path) = stack.removeLast()

                // in order to make a loop, the path should be at least 3
                if (currentPipe.pipeChar == 'S' && path.size >= 3) return path
                if (currentPipe in visited) continue

                for (p in getValidNeighbours(currentPipe)) {
                    stack.add(Triple(p, visited + currentPipe, path + currentPipe))
                }
            }

            return null
        }

        fun isCellEnclosed(x: Int, y: Int): Boolean {
            if (grid[y][x].pipeChar != EMPTY_CELL_CHAR) return false

            val crossingPipes = if (x < width / 2) {
                grid[y].subList(0, x).count {
                    it.pipeChar in PIPES_TO_NORTH
                }
            }
            else {
                grid[y].subList(x, this.width).count {
                    it.pipeChar in PIPES_TO_NORTH
                }
            }

            if (crossingPipes.mod(2) == 1) {
                this.grid[y][x].pipeChar = FILLED_CELL_CHAR
                return true
            }
            return false
        }

        fun print() {
            this.grid.forEach { line ->
                println(line.joinToString { it.pipeChar.toString() })
            }
            println()
        }
    }

    class Pipe(val x: Int, val y: Int, var pipeChar: Char) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Pipe

            if (x != other.x) return false
            if (y != other.y) return false
            if (pipeChar != other.pipeChar) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + pipeChar.hashCode()
            return result
        }

        override fun toString(): String {
            return "Pipe '$pipeChar' ($x|$y) "
        }
    }

    override fun part1(): Int {
        val maze = Maze(inputAsGrid)

        val start = maze.grid.flatten().first {
            it.pipeChar == 'S'
        }

        val path = maze.getLoop(start) ?: return -1

        return path.size / 2
    }

    override fun part2(): Int {
        val maze = Maze(inputAsGrid)

        val start = maze.grid.flatten().first {
            it.pipeChar == 'S'
        }
        val path = maze.getLoop(start) ?: return -1

        // remove unconnected pipe parts
        maze.grid.flatten().forEach {
            if (!path.contains(it)) {
                maze.grid[it.y][it.x].pipeChar = maze.EMPTY_CELL_CHAR
            }
        }

        val enclosed = maze.grid.flatten().count {
            maze.isCellEnclosed(it.x, it.y)
        }

        maze.print()

        return enclosed
    }

}

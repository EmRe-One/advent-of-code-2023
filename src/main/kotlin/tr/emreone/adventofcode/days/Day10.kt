package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Day10 : Day(
    10,
    2023,
    "Pipe Maze",
    session = Resources.resourceAsString("session.cookie")
) {

    class PipeMaze(input: List<List<Char>>) {
        val width = input.first().size
        val height = input.size
        val grid = input.mapIndexed { y, line ->
            line.mapIndexed { x, p ->
                Pipe(x, y, p)
            }
        }
    }

    class Pipe(val x: Int, val y: Int, val pipe: Char) {
        val allowedPipesInDirection: Map<Char, List<Char>> = mapOf(
            'n' to listOf('|', '7', 'F', 'S'),
            'e' to listOf('-', 'J', '7', 'S'),
            's' to listOf('|', 'L', 'J', 'S'),
            'w' to listOf('-', 'F', 'L', 'S')
        )

        private fun getNeighbours(maze: PipeMaze): Map<Char, Pipe> {
            return buildMap {
                if (this@Pipe.y > 0) {
                    this['n'] = maze.grid[y - 1][x]
                }
                if (this@Pipe.x < maze.width - 1) {
                    this['e'] = maze.grid[y][x + 1]
                }
                if (this@Pipe.y < maze.height - 1) {
                    this['s'] = maze.grid[y + 1][x]
                }
                if (this@Pipe.x > 0) {
                    this['w'] = maze.grid[y][x - 1]
                }
            }
        }

        fun getValidNeighbours(maze: PipeMaze): List<Pipe> {
            val neighboursCompass = getNeighbours(maze).filter {
                it.value.pipe != '.'
            }
            val lookAtDirection = when (this.pipe) {
                'S' -> listOf('n', 'e', 's', 'w')
                '|' -> listOf('n', 's')
                '-' -> listOf('e', 'w')
                'L' -> listOf('n', 'e')
                'J' -> listOf('n', 'w')
                '7' -> listOf('s', 'w')
                'F' -> listOf('s', 'e')
                else -> throw IllegalArgumentException()
            }
            return neighboursCompass.mapNotNull {
                if (it.key in lookAtDirection && it.value.pipe in allowedPipesInDirection[it.key]!!) {
                    it.value
                }
                else {
                    null
                }
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Pipe

            if (x != other.x) return false
            if (y != other.y) return false
            if (pipe != other.pipe) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + pipe.hashCode()
            return result
        }

        override fun toString(): String {
            return "Pipe ($x|$y) with $pipe"
        }
    }

    fun loopFinder(maze: PipeMaze, prevPipe: Pipe?, currentPipe: Pipe, visited: List<Pipe>, path: List<Pipe>): List<Pipe>? {
        val neighbours = currentPipe.getValidNeighbours(maze)

        val unVisitedNeighbours = neighbours.filter { it !in visited && it != prevPipe }
        if (unVisitedNeighbours.isEmpty()) return null

        val newVisited = visited.toMutableList()
        if (currentPipe.pipe != 'S') {
            newVisited.add(currentPipe)
        }

        return unVisitedNeighbours.firstNotNullOfOrNull {
            if (it.pipe == 'S' && path.size > 3) {
                return@firstNotNullOfOrNull path + currentPipe
            }
            return@firstNotNullOfOrNull loopFinder(maze, currentPipe, it, newVisited, path + currentPipe)
        }
    }

    fun loopFinder(maze: PipeMaze, startPipe: Pipe): List<Pipe>? {
        val queue = LinkedList<Pair<Pipe, List<Pipe>>>()
        val visited = mutableSetOf<Pipe>()
        queue.add(Pair(startPipe, listOf(startPipe)))

        while (queue.isNotEmpty()) {
            val (currentPipe, path) = queue.poll()

            if (currentPipe.pipe == 'S' && path.size > 3) {
                return path
            }

            if (currentPipe.pipe == 'S') {
                visited.add(currentPipe)
            }

            val neighbours = currentPipe.getValidNeighbours(maze)

            for (neighbour in neighbours) {
                if (neighbour !in visited) {
                    val newPath = path + neighbour
                    queue.add(Pair(neighbour, newPath))
                }
            }
        }

        return null
    }

    override fun part1(): Int {
        val maze = PipeMaze(inputAsGrid)

        val start = maze.grid.flatten().first {
            it.pipe == 'S'
        }

        val visited = emptyList<Pipe>()
        val path = loopFinder(maze, start)

        path?.forEach {
            println(it)
        }
        return (path?.size ?: 0) / 2
    }

    override fun part2(): Long {
        return 0L
    }

}
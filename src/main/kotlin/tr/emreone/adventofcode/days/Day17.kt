package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Coords
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point2D
import tr.emreone.kotlin_utils.math.plus
import java.util.*

class Day17 : Day(17, 2023, "Clumsy Crucible") {

    data class Node(val point: Point2D, val direction: Direction4?, val steps: Int)

    class HeatLossMap(input: List<String>) {
        val map = input.map { line ->
            line.map {
                it.digitToInt()
            }
        }
        val height = this.map.size
        val width = this.map[0].size

        fun adjustedDijkstra(): Int {
            val dist = Array(height) { Array(width) { Int.MAX_VALUE } }
            val pq = PriorityQueue<Triple<Node, Int, Int>>(compareBy { it.second })

            // Start node with 0 heat loss
            pq.add(Triple(Node(Point2D(0, 0), null, 0), 0, 0))
            dist[0][0] = 0

            while (pq.isNotEmpty()) {
                val (current, _, _) = pq.poll()

                val directions = arrayOf(
                    Pair(-1, 0) to Direction4.NORTH, // Up
                    Pair(1, 0) to Direction4.SOUTH,  // Down
                    Pair(0, -1) to Direction4.WEST,  // Left
                    Pair(0, 1) to Direction4.EAST    // Right
                )

                for ((d, newDirection) in directions) {
                    val newX = (current.point.x + d.first).toInt()
                    val newY = (current.point.y + d.second).toInt()

                    if (newX in 0 until height && newY in 0 until width) {
                        val newSteps = if (current.direction == null || current.direction == newDirection) {
                            current.steps + 1
                        }
                        else {
                            1
                        }

                        if (newSteps <= 3) { // Check step constraint
                            val newDist = dist[current.point.x.toInt()][current.point.y.toInt()] + map[newX][newY]
                            if (newDist < dist[newX][newY]) {
                                dist[newX][newY] = newDist
                                pq.add(
                                    Triple(Node(Point2D(newX.toLong(), newY.toLong()), newDirection, newSteps), newDist, newSteps)
                                )
                            }
                        }
                    }
                }
            }

            return dist[height - 1][width - 1] // Return the heat loss to reach the destination
        }

    }

    override fun part1(): Int {
        return HeatLossMap(inputAsList)
            .adjustedDijkstra()
    }

}

package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.data_structures.Dijkstra
import tr.emreone.kotlin_utils.data_structures.Graph
import tr.emreone.kotlin_utils.extensions.area
import tr.emreone.kotlin_utils.extensions.formatted
import tr.emreone.kotlin_utils.math.*

class Day17 : Day(17, 2023, "Clumsy Crucible") {

    val heatMap = inputAsGrid.map { it.map { it.digitToInt() } }
    val area = heatMap.area

    data class State(
        val pos: Point,
        val movedStraight: Int,
        val dir: Direction4?,
    )

    override fun part1(): Int {

        val graph = object : Graph<State> {
            override fun neighborsOf(node: State): Collection<State> {
                return Direction4.all
                    .map { d ->
                        State(
                            node.pos + d,
                            movedStraight = if (d == node.dir) node.movedStraight + 1 else 1,
                            dir = d
                        )
                    }
                    .filter {
                        it.pos in area
                                && it.movedStraight <= 3
                                && it.dir != node.dir?.opposite
                    }
            }

            override fun cost(from: State, to: State): Int =
                heatMap[to.pos.y][to.pos.x]
        }

        val start = State(origin, 0, null)

        val x = Dijkstra(start, graph::neighborsOf, graph::cost).search { it.pos == area.lowerRight }

        val path = x.path
        heatMap.formatted { pos, v ->
            if (pos in path.map { it.pos })
                path.first { it.pos == pos }.movedStraight.toString()
            else "."
        }
        return path.drop(1).sumOf { heatMap[it.pos.y][it.pos.x] }
    }

    override fun part2(): Int {

        val graph = object : Graph<State> {
            override fun neighborsOf(node: State): Collection<State> {
                return (if (node.movedStraight < 4 && node.pos != origin)
                    listOf(node.copy(node.pos + node.dir!!, movedStraight = node.movedStraight + 1))
                else Direction4.all.map { d ->
                    State(
                        node.pos + d,
                        movedStraight = if (d == node.dir) node.movedStraight + 1 else 1,
                        dir = d
                    )
                }).filter { it.pos in area && it.movedStraight <= 10 && it.dir != node.dir?.opposite }
            }

            override fun cost(from: State, to: State): Int =
                heatMap[to.pos.y][to.pos.x]

            override fun costEstimation(from: State, to: State): Int =
                from.pos manhattanDistanceTo to.pos
        }

        val start = State(origin, 0, null)

        val x = Dijkstra(start, graph::neighborsOf, graph::cost).search { it.pos == area.lowerRight && it.movedStraight >= 4 }

        val path = x.path
        heatMap.formatted { pos, v ->
            if (pos in path.map { it.pos })
                path.first { it.pos == pos }.movedStraight.toString()
            else "."
        }

        return path.drop(1).sumOf { heatMap[it.pos.y][it.pos.x] }
    }

}

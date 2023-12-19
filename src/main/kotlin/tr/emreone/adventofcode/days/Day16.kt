package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Coords
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.plus

class Day16 : Day(16, 2023, "The Floor Will Be Lava") {

    class Tile(var tile: Char) {
        var energized: Boolean = false
        val rayDirections: MutableList<Direction4> = mutableListOf()
    }

    class Grid(input: List<String>) {
        val tiles = input.map { line ->
            line.map {
                Tile(it)
            }
        }

        companion object {
            const val MIRROR_TL_BR = '\\'
            const val MIRROR_TR_BL = '/'
            const val SPLITTER_HORIZONTAL = '-'
            const val SPLITTER_VERTICAL = '|'
            const val EMPTY = '.'
        }

        fun raytrace(position: Coords, direction: Direction4) {
            if (position.second !in this.tiles.indices
                || position.first !in this.tiles[0].indices
            ) {
                return
            }

            val tile = this.tiles[position.second][position.first]
            tile.energized = true
            if (tile.rayDirections.contains(direction)) {
                return
            }
            else {
                tile.rayDirections.add(direction)
            }

            val nextDirections = buildList {
                when (tile.tile) {
                    EMPTY               -> add(direction)
                    SPLITTER_VERTICAL   -> addAll(listOf(Direction4.SOUTH, Direction4.NORTH))
                    SPLITTER_HORIZONTAL -> addAll(listOf(Direction4.EAST, Direction4.WEST))
                    MIRROR_TR_BL        -> {
                        when (direction) {
                            Direction4.EAST  -> add(Direction4.NORTH)
                            Direction4.SOUTH -> add(Direction4.WEST)
                            Direction4.WEST  -> add(Direction4.SOUTH)
                            Direction4.NORTH -> add(Direction4.EAST)
                        }
                    }

                    MIRROR_TL_BR        -> {
                        when (direction) {
                            Direction4.EAST  -> add(Direction4.SOUTH)
                            Direction4.SOUTH -> add(Direction4.EAST)
                            Direction4.WEST  -> add(Direction4.NORTH)
                            Direction4.NORTH -> add(Direction4.WEST)
                        }
                    }
                }
            }

            for (d in nextDirections) {
                raytrace(position.plus(d), d)
            }
        }

        fun raytraceIterative(initialPosition: Coords, initialDirection: Direction4) {
            val stack = mutableListOf(Pair(initialPosition, initialDirection))

            while (stack.isNotEmpty()) {
                val (position, direction) = stack.removeAt(stack.size - 1)

                if (position.second !in this.tiles.indices || position.first !in this.tiles[0].indices) {
                    continue
                }

                val tile = this.tiles[position.second][position.first]
                tile.energized = true
                if (tile.rayDirections.contains(direction)) {
                    continue
                }
                else {
                    tile.rayDirections.add(direction)
                }

                val nextDirections = buildList {
                    when (tile.tile) {
                        EMPTY               -> add(direction)
                        SPLITTER_VERTICAL   -> addAll(listOf(Direction4.SOUTH, Direction4.NORTH))
                        SPLITTER_HORIZONTAL -> addAll(listOf(Direction4.EAST, Direction4.WEST))
                        MIRROR_TR_BL        -> {
                            when (direction) {
                                Direction4.EAST  -> add(Direction4.NORTH)
                                Direction4.SOUTH -> add(Direction4.WEST)
                                Direction4.WEST  -> add(Direction4.SOUTH)
                                Direction4.NORTH -> add(Direction4.EAST)
                            }
                        }

                        MIRROR_TL_BR        -> {
                            when (direction) {
                                Direction4.EAST  -> add(Direction4.SOUTH)
                                Direction4.SOUTH -> add(Direction4.EAST)
                                Direction4.WEST  -> add(Direction4.NORTH)
                                Direction4.NORTH -> add(Direction4.WEST)
                            }
                        }
                    }
                }

                for (d in nextDirections) {
                    stack.add(position.plus(d) to d)
                }
            }
        }

        fun show() {
            for (line in this.tiles) {
                for (t in line) {
                    when (t.tile) {
                        EMPTY -> {
                            if (t.energized) {
                                if (t.rayDirections.size == 1) {
                                    when (t.rayDirections.first()) {
                                        Direction4.EAST  -> print('>')
                                        Direction4.SOUTH -> print('v')
                                        Direction4.WEST  -> print('<')
                                        Direction4.NORTH -> print('^')
                                    }
                                }
                                else {
                                    print(t.rayDirections.size)
                                }
                            }
                            else {
                                print(EMPTY)
                            }
                        }

                        else  -> print(t.tile)
                    }
                }
                println()
            }
        }
    }

    override fun part1(): Int {
        val grid = Grid(inputAsList)

        grid.raytraceIterative(Coords(0, 0), Direction4.EAST)
        grid.show()

        return grid.tiles.flatten().count { it.energized }
    }

    override fun part2(): Int {
        val width = inputAsList[0].length
        val height = inputAsList.size

        var maxEnergie = Int.MIN_VALUE

        for (x in 0 until width) {
            val grid = Grid(inputAsList)
            grid.raytraceIterative(Coords(x, 0), Direction4.SOUTH)
            val energie = grid.tiles.flatten().count { it.energized }
            if (energie > maxEnergie) {
                maxEnergie = energie
            }
        }
        for (x in 0 until width) {
            val grid = Grid(inputAsList)
            grid.raytraceIterative(Coords(x, height - 1), Direction4.NORTH)
            val energie = grid.tiles.flatten().count { it.energized }
            if (energie > maxEnergie) {
                maxEnergie = energie
            }
        }

        for (y in 0 until height) {
            val grid = Grid(inputAsList)
            grid.raytraceIterative(Coords(0, y), Direction4.EAST)
            val energie = grid.tiles.flatten().count { it.energized }
            if (energie > maxEnergie) {
                maxEnergie = energie
            }
        }
        for (y in 0 until height) {
            val grid = Grid(inputAsList)
            grid.raytraceIterative(Coords(width-1, y), Direction4.WEST)
            val energie = grid.tiles.flatten().count { it.energized }
            if (energie > maxEnergie) {
                maxEnergie = energie
            }
        }

        return maxEnergie
    }
}

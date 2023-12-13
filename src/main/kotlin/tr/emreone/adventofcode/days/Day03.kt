package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

class Day03 : Day(3, 2023, "Gear Ratios") {

    class Part(val x: Int, val y: Int, val value: Int) {
        fun getNeighbourFields(engine: Engine): List<Char> {
            val neighbours = mutableListOf<Char>()
            /*
                We want to return all the neighbours of the given Part with the Value NNN.
                The first digit has the coordinates x, y
                . . . . .
                . N N N .
                . . . . .
             */
            val valueLength = value.toString().length
            for (dx in -1..valueLength) {
                val x = this.x + dx
                for (dy in -1..1) {
                    val y = this.y + dy

                    // ignore the center part
                    if (dy == 0 && dx != -1 && dx != valueLength) {
                        continue
                    }

                    if (y in engine.field.indices) {
                        if (x in engine.field[y].indices) {
                            neighbours.add(engine.field[y][x])
                        }
                    }
                }
            }

            return neighbours
        }

        fun isAdjacentTo(x: Int, y: Int): Boolean {
            val valueLength = value.toString().length
            return x in (this.x - 1)..(this.x + valueLength)
                    && y in (this.y - 1)..(this.y + 1)
        }
    }

    class Engine(val field: List<CharArray>) {
        companion object {
            fun parse(input: List<String>): Engine {
                val field = input.map { line -> line.toCharArray() }
                return Engine(field)
            }
        }

        val parts = mutableListOf<Part>()

        init {
            for (y in this.field.indices) {
                var startX = 0
                while (startX < this.field[y].size) {
                    val currentChar = this.field[y][startX]
                    if (currentChar.isDigit()) {
                        var endX = startX + 1
                        while (endX in this.field[y].indices && this.field[y][endX].isDigit()) {
                            endX++
                        }
                        val value = this.field[y].joinToString("")
                            .substring(startX until endX).toInt()
                        this.parts.add(Part(startX, y, value))
                        startX = endX
                    } else {
                        startX++
                    }
                }
            }
        }
    }

    override fun part1(): Int {
        val engine = Engine.parse(inputAsList)

        val filteredParts = engine.parts.filter { part ->
            part.getNeighbourFields(engine).any {
                !it.isDigit() && it != '.'
            }
        }

        return filteredParts.sumOf { it.value }
    }

    override fun part2(): Long {
        val engine = Engine.parse(inputAsList)

        var sum = 0L
        engine.field.forEachIndexed { y, line ->
            line.forEachIndexed { x, field ->
                if (field == '*') {
                    val subParts = engine.parts.filter {
                        it.isAdjacentTo(x, y)
                    }
                    if (subParts.size == 2) {
                        sum += subParts[0].value * subParts[1].value
                    }
                }
            }
        }
        return sum
    }

}

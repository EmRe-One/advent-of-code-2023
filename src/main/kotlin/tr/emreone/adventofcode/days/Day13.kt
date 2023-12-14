package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import java.lang.IllegalStateException

class Day13 : Day(13, 2023, "Point of Incidence") {

    class Grid(input: String) {
        val area: List<List<Char>>
        var horizontalLineIndex: Int? = null
        var verticalLineIndex: Int? = null

        init {
            this.area = input.split("\\r?\\n".toRegex())
                .map {
                    it.toCharArray().map { it }
                }
        }

        private fun getColumn(x: Int): List<Char> {
            if (x !in this.area.get(0).indices) {
                throw IndexOutOfBoundsException()
            }
            return (this.area).map {
                it[x]
            }
        }

        private fun getRow(y: Int): List<Char> {
            return this.area.get(y)
        }

        private fun isVerticalReflectedAt(yi: Int): Boolean {
            if (yi < 0 || yi == this.area.lastIndex) return false

            val maxHeight = minOf(yi, this.area.lastIndex - 1 - yi)
            return (0..maxHeight).all { i ->
                val row1 = getRow(yi - i)
                val row2 = getRow(yi + 1 + i)
                // println("Comparing y=${yi - i} ($row1) with y=${yi + 1 + i} ($row2)")
                row1.equals(row2)
            }
        }

        private fun isHorizontalReflectedAt(xi: Int): Boolean {
            if (xi < 0 || xi == this.area.get(0).lastIndex) return false

            val maxWidth = minOf(xi, this.area.get(0).lastIndex - 1 - xi)
            return (0..maxWidth).all { i ->
                val col1 = getColumn(xi - i)
                val col2 = getColumn(xi + 1 + i)
                // println("Comparing x=${xi - i} ($col1) with x=${xi + 1 + i} ($col2)")
                col1.equals(col2)
            }
        }

        fun getScore(originalGrid: Grid? = null): Int {
            val possibleVerticalLines  = (0 until this.area.lastIndex).filter {
                isVerticalReflectedAt(it)
            }
            if (possibleVerticalLines.isNotEmpty()) {
                this.verticalLineIndex = possibleVerticalLines.firstOrNull {
                    it != originalGrid?.verticalLineIndex
                }
                if (this.verticalLineIndex != null) {
                    return (this.verticalLineIndex!! + 1) * 100
                }
            }

            val possibleHorizontalLines = (0 until this.area.get(0).lastIndex).filter {
                isHorizontalReflectedAt(it)
            }
            if (possibleHorizontalLines.isNotEmpty()) {
                this.horizontalLineIndex = possibleHorizontalLines.firstOrNull {
                    it != originalGrid?.horizontalLineIndex
                }
                if (this.horizontalLineIndex != null) {
                    return this.horizontalLineIndex!! + 1
                }
            }

            return 0
        }

        fun getCopyWithToggledCell(xi: Int, yi: Int): Grid {
            val aGrid = this.area.map { it.toMutableList() }
            aGrid[yi][xi] = if (aGrid[yi][xi] == '.') '#' else '.'

            return Grid(aGrid.joinToString("\n") { it.joinToString("") })
        }

        fun getAlternativeScore(): Int {
            for(yi in this.area.indices) {
                for(xi in this.area.get(yi).indices) {
                    val alternativeGrid = getCopyWithToggledCell(xi, yi)
                    val aScore = alternativeGrid.getScore(this)

                    if (aScore > 0) {
                        return aScore
                    }
                }
            }

            throw IllegalStateException("No alternative score found.")
        }

        fun print() {
            for(y in this.area.indices) {
                println(this.area[y].joinToString(""))
            }
        }
    }

    override fun part1(): Int {
        return inputAsString.split("(\\r?\\n){2}".toRegex())
            .sumOf {
                Grid(it).getScore()
            }
    }

    override fun part2(): Int {
        return inputAsString.split("(\\r?\\n){2}".toRegex())
            .sumOf {
                val grid = Grid(it)
                val score = grid.getScore()
                grid.getAlternativeScore()
            }
    }

}

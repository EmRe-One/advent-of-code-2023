package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

class Day25 : Day(25, 2023, "") {

    override fun part1(): Int {
        while (true) {
            val graph = mutableMapOf<String, MutableList<String>>()
            inputAsList.forEach { row ->
                val sourceName = row.substringBefore(":")
                val source = graph.getOrPut(sourceName) { mutableListOf() }
                row.substringAfter(":").trim().split(" ").forEach { connection ->
                    source += connection
                    graph.getOrPut(connection) { mutableListOf() }.add(sourceName)
                }
            }

            val counts = graph.keys.associateWith { 1 }.toMutableMap()

            while (graph.size > 2) {
                val a = graph.keys.random()
                val b = graph.getValue(a).random()
                val newNode = "$a-$b"

                counts[newNode] = (counts.remove(a) ?: 0) + (counts.remove(b) ?: 0)
                graph.combineValues(a, b, newNode)
                graph.mergeNodes(a, newNode)
                graph.mergeNodes(b, newNode)
            }

            val (nodeA, nodeB) = graph.keys.toList()
            if (graph.getValue(nodeA).size == 3) {
                return counts.getValue(nodeA) * counts.getValue(nodeB)
            }
        }
    }

    private fun MutableMap<String, MutableList<String>>.combineValues(a: String, b: String, newNode: String) {
        this[newNode] = (this.getValue(a).filter { it != b } + this.getValue(b).filter { it != a }).toMutableList()
    }

    private fun MutableMap<String, MutableList<String>>.mergeNodes(oldNode: String, newNode: String) {
        remove(oldNode)?.forEach { target ->
            getValue(target).replaceAll { if (it == oldNode) newNode else it }
        }
    }

}

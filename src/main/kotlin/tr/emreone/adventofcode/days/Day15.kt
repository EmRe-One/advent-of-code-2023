package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day

class Day15 : Day(15, 2023, "Lens Library") {

    class Lens(val label: String, val focalLength: Int? = null) {

        override fun hashCode(): Int {
            var hash = 0

            this.label.forEach {
                hash += it.code
                hash *= 17
                hash = hash.mod(256)
            }

            return hash
        }
    }

    override fun part1(): Long {
        return inputAsString
            .replace("\\s+".toRegex(), "")
            .split(",")
            .sumOf {
                Lens(it, null).hashCode().toLong()
            }
    }

    override fun part2(): Long {
        val boxes = mutableMapOf<Int, MutableList<Lens>>()

        inputAsString
            .replace("\\s+".toRegex(), "")
            .split(",")
            .forEach {
                val index = it.indexOfAny(listOf("=", "-"), 0)
                val operation = it.get(index)
                val (label, focalLength) = it.split(operation)

                val lens = Lens(label, focalLength.takeIf { it.isNotEmpty() }?.toInt())
                when (operation) {
                    '-' -> {
                        boxes.get(lens.hashCode())
                            ?.removeAll { l ->
                                l.label == label
                            }
                    }

                    '=' -> {
                        val box = boxes.getOrDefault(lens.hashCode(), mutableListOf())

                        val i = box.indexOfFirst { it.label == label }
                        if (i >= 0) {
                            box.removeAt(i)
                            box.add(i, lens)
                        }
                        else {
                            box.add(lens)
                        }

                        boxes[lens.hashCode()] = box
                    }
                }
            }

        return boxes.filterValues { it.isNotEmpty() }
            .entries
            .sumOf { (indexBox, box) ->
                box.mapIndexed { indexLens, lens ->
                    (indexBox + 1) * (indexLens + 1) * lens.focalLength!!.toLong()
                }.sum()
            }
    }
}
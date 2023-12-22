package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.extractAllIntegers
import tr.emreone.kotlin_utils.math.Coords
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.plus

class Day19 : Day(19, 2023, "Aplenty") {

    private val workflows = inputAsGroups[0].map {
        val name = it.substringBefore('{')
        val rules = it.substringAfter('{').dropLast(1).split(',').map { r ->
            if (':' in r) {
                val (c, n) = r.split(':')
                val category = when (c[0]) {
                    'x'  -> 0
                    'm'  -> 1
                    'a'  -> 2
                    's'  -> 3
                    else -> error(r)
                }
                when (c[1]) {
                    '>'  -> Rule.GreaterThan(category, c.first { it.isDigit() }.digitToInt(), n)
                    '<'  -> Rule.LessThan(category, c.first { it.isDigit() }.digitToInt(), n)
                    else -> error(r)
                }
            }
            else
                Rule.Unconditional(r)
        }
        Workflow(name, rules)
    }.associateBy { it.name }.show()

    private val parts = inputAsGroups[1].map { it.extractAllIntegers() }.show()

    data class Workflow(val name: String, val rules: List<Rule>)

    sealed interface Rule {
        val next: String
        fun matches(part: List<Int>): Boolean
        fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?>

        data class LessThan(val category: Int, val value: Int, override val next: String) : Rule {
            override fun matches(part: List<Int>) = part[category] < value

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> {
                val relevant = parts[category]
                val (matching, notMatching) =
                    when {
                        value in relevant     ->
                            (relevant.first..<value) to (value..relevant.last)

                        value > relevant.last ->
                            relevant to null

                        else                  -> null to null
                    }
                return parts.patch(category, matching) to parts.patch(category, notMatching)
            }
        }

        data class GreaterThan(val category: Int, val value: Int, override val next: String) : Rule {
            override fun matches(part: List<Int>) = part[category] > value

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> {
                val relevant = parts[category]
                val (matching, notMatching) =
                    when {
                        value in relevant      ->
                            ((value + 1)..relevant.last) to (relevant.first..value)

                        value < relevant.first ->
                            relevant to null

                        else                   -> null to null
                    }
                return parts.patch(category, matching) to parts.patch(category, notMatching)
            }
        }

        data class Unconditional(override val next: String) : Rule {
            override fun matches(part: List<Int>) = true

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> =
                parts to null
        }

        fun List<IntRange>.patch(category: Int, newRange: IntRange?): List<IntRange>? =
            newRange?.let { patched ->
                mapIndexed { index, range ->
                    patched.takeIf { index == category } ?: range
                }
            }
    }

    override fun part1(): Int {

        fun List<Int>.isAccepted(): Boolean {
            var wf = "in"
            while (true) {
                if (wf == "R") return false
                if (wf == "A") return true
                wf = workflows[wf]!!.rules.first { r -> r.matches(this) }.next
            }
        }

        return parts.filter { it.isAccepted() }.sumOf { it.sum() }
    }

    override fun part2(): Long {
        val potentialPart = listOf(
            1..4000,
            1..4000,
            1..4000,
            1..4000,
        )

        fun countAccepted(wf: Workflow, parts: PotentialPart?): Long =
            wf.rules.fold(parts to 0L) { (remaining, count), rule ->
                if (remaining != null) {
                    val (matching, notMatching) = rule.split(remaining)
                    notMatching to count + when (rule.next) {
                        "A"  -> matching?.combinations() ?: 0
                        "R"  -> 0
                        else -> countAccepted(workflows[rule.next]!!, matching)
                    }
                }
                else
                    null to count
            }.second

        return countAccepted(workflows["in"]!!, potentialPart)
    }

    private fun PotentialPart.combinations(): Long =
        this.map { r -> r.last - r.first + 1L }
            .reduce { acc, i -> acc * i }

}

typealias PotentialPart = List<IntRange>

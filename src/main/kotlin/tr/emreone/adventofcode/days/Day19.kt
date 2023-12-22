package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.extractAllIntegers

enum class Category(name: String) {
    X("Extremely cool looking"),
    M("Musical"),
    A("Aerodynamic"),
    S("Shiny")
}

typealias PotentialPart = Map<Category, IntRange>

fun PotentialPart.patch(category: Category, newRange: IntRange?): PotentialPart? {
    return newRange?.let { patched ->
        val parts = this.toMutableMap()
        parts[category] = patched

        parts.toMap()
    }
}

fun PotentialPart.combinations(): Long {
    return this.values.fold(1) { acc, i ->
        acc * (i.last - i.first + 1L)
    }
}

class Day19 : Day(19, 2023, "Aplenty") {

    sealed interface Rule {
        val next: String
        fun matches(part: Map<Category, Int>): Boolean
        fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?>

        data class LessThan(val category: Category, val value: Int, override val next: String) : Rule {
            override fun matches(part: Map<Category, Int>) = part[category]!! < value

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> {
                val relevant = parts[category]!!
                val (matching, notMatching) =
                    when {
                        value in relevant ->
                            (relevant.first..<value) to (value..relevant.last)

                        value > relevant.last ->
                            relevant to null

                        else -> null to null
                    }
                return parts.patch(category, matching) to parts.patch(category, notMatching)
            }
        }

        data class GreaterThan(val category: Category, val value: Int, override val next: String) : Rule {
            override fun matches(part: Map<Category, Int>) = part[category]!! > value

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> {
                val relevant = parts[category]!!
                val (matching, notMatching) =
                    when {
                        value in relevant ->
                            ((value + 1)..relevant.last) to (relevant.first..value)

                        value < relevant.first ->
                            relevant to null

                        else -> null to null
                    }
                return parts.patch(category, matching) to parts.patch(category, notMatching)
            }
        }

        data class Unconditional(override val next: String) : Rule {
            override fun matches(part: Map<Category, Int>) = true

            override fun split(parts: PotentialPart): Pair<PotentialPart?, PotentialPart?> =
                parts to null
        }
    }

    data class Workflow(val name: String, val rules: List<Rule>)

    private val workflows = inputAsGroups[0]
        .map {
            val (name, ruleString) = it.split('{')
            val rules = ruleString.dropLast(1)
                .split(',')
                .map { r ->
                    if (':' in r) {
                        val (c, next) = r.split(':')
                        val category = when (c[0]) {
                            'x' -> Category.X
                            'm' -> Category.M
                            'a' -> Category.A
                            's' -> Category.S
                            else -> error(r)
                        }
                        when (c[1]) {
                            '>' -> Rule.GreaterThan(category, c.split('>')[1].toInt(), next)
                            '<' -> Rule.LessThan(category, c.split('<')[1].toInt(), next)
                            else -> error(r)
                        }
                    } else {
                        Rule.Unconditional(r)
                    }
                }

            Workflow(name, rules)
        }
        .associateBy { it.name }
        .show()

    private val partRatings = inputAsGroups[1]
        .map {
            Category.entries.zip(it.extractAllIntegers()).toMap()
        }
        .show()

    override fun part1(): Int {
        fun Map<Category, Int>.isAccepted(): Boolean {
            var wf = "in"
            while (true) {
                if (wf == "R") return false
                if (wf == "A") return true
                wf = workflows[wf]!!.rules.first { r -> r.matches(this) }.next
            }
        }

        return partRatings
            .filter { it.isAccepted() }
            .sumOf { it.values.sum() }
    }

    override fun part2(): Long {
        val potentialParts = Category.entries.associateWith { 1..4000 }

        fun countAccepted(wf: Workflow, parts: PotentialPart?): Long =
            wf.rules.fold(parts to 0L) { (remaining, count), rule ->
                if (remaining != null) {
                    val (matching, notMatching) = rule.split(remaining)
                    notMatching to count + when (rule.next) {
                        "A" -> matching?.combinations() ?: 0
                        "R" -> 0
                        else -> countAccepted(workflows[rule.next]!!, matching)
                    }
                } else {
                    null to count
                }
            }.second

        return countAccepted(workflows["in"]!!, potentialParts)
    }

}

package tr.emreone.adventofcode.days

interface IDay<in I, out O> {

    fun part1(input: I): O
    fun part2(input: I): O

}
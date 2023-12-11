![Kotlin](https://img.shields.io/badge/Kotlin-grey?logo=Kotlin&style=for-the-badge)
![](https://img.shields.io/badge/📅%20days-11-005060?style=for-the-badge)
![](https://img.shields.io/badge/⭐%20stars-20-005060?style=for-the-badge)

# Advent-of-Code 2023

## Intro

You can find here my [Advent-of-Code 2023](https://adventofcode.com/2023) puzzle solutions written in Kotlin.

## Scripts

To prepare Boilerplate code for the next day, just execute the following gradle task with the day number
```bash 
gradle prepareNextDay -Pday=1
```

To generate the aoc_tiles, navigate to the aoc_tiles dir, activate python venv and the run the python script:
```bash
cd aoc_tiles

# because i am using powershell
./venv/Scripts/activate.ps1 

python ./create_tiles.py
```

## Solutions

| Day                                           | Test                                                                        | Solution                                                            | Tile                               |
|-----------------------------------------------|-----------------------------------------------------------------------------|---------------------------------------------------------------------|------------------------------------|
| [Day 01](https://adventofcode.com/2023/day/1) | [Day01Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day01Test.kt) | [Day01.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day01.kt) | ![Day 01](./aoc_tiles/2023/01.png) |
| [Day 02](https://adventofcode.com/2023/day/2) | [Day02Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day02Test.kt) | [Day02.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day02.kt) | ![Day 02](./aoc_tiles/2023/02.png) |
| [Day 03](https://adventofcode.com/2023/day/3) | [Day03Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day03Test.kt) | [Day03.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day03.kt) | ![Day 03](./aoc_tiles/2023/03.png) |
| [Day 04](https://adventofcode.com/2023/day/4) | [Day04Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day04Test.kt) | [Day04.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day04.kt) | ![Day 04](./aoc_tiles/2023/04.png) |
| [Day 05](https://adventofcode.com/2023/day/5) | [Day05Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day05Test.kt) | [Day05.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day05.kt) | ![Day 05](./aoc_tiles/2023/05.png) |
| [Day 06](https://adventofcode.com/2023/day/6) | [Day06Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day06Test.kt) | [Day06.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day06.kt) | ![Day 06](./aoc_tiles/2023/06.png) |
| [Day 07](https://adventofcode.com/2023/day/7) | [Day07Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day07Test.kt) | [Day07.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day07.kt) | ![Day 07](./aoc_tiles/2023/07.png) |
| [Day 08](https://adventofcode.com/2023/day/8) | [Day08Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day08Test.kt) | [Day08.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day08.kt) | ![Day 08](./aoc_tiles/2023/08.png) |
| [Day 09](https://adventofcode.com/2023/day/9) | [Day09Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day09Test.kt) | [Day09.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day09.kt) | ![Day 09](./aoc_tiles/2023/09.png) |
<!-- $1 -->

---

** The AoC Tiles are created with an adjusted python script from [LiquidFun](https://github.com/LiquidFun/adventofcode/tree/main/AoCTiles)

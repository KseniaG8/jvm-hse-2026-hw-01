package app

import saving.SaverRegistry

fun main(args: Array<String>) {
    val registry = SaverRegistry.default()

    val input = args.getOrNull(0) ?: "input.csv"
    val format = args.getOrNull(1) ?: run {
        println("Доступные форматы: ${registry.availableFormats()}")
        print("Введите формат: "); readln().trim().lowercase()
    }
    val output = args.getOrNull(2) ?: "output.$format"

    Application(registry).run(input, format, output)
}
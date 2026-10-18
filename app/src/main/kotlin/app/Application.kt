package app

import model.ParseError
import model.Product
import parsing.CsvParser
import parsing.ParseResult
import parsing.ProductParser
import saving.SaverRegistry
import stats.ProcessingStats
import java.io.File

class Application(private val registry: SaverRegistry) {

    fun run(inputPath: String, outputFormat: String, outputPath: String) {
        val saver = registry.get(outputFormat)
            ?: error("Неизвестный формат '$outputFormat'. Доступно: ${registry.availableFormats()}")

        val lines = File(inputPath).readLines()
        val header = lines.firstOrNull() ?: error("Файл пуст")
        require(header.startsWith("id,name,type")) { "Некорректный заголовок: $header" }

        val products = mutableListOf<Product>()
        val errors = mutableListOf<ParseError>()
        val dataLines = lines.drop(1).filter { it.isNotBlank() }

        dataLines.forEachIndexed { index, line ->
            val lineNumber = index + 1
            when (val result = ProductParser.parse(CsvParser.parseLine(line))) {
                is ParseResult.Success -> products += result.product
                is ParseResult.Failure -> errors += ParseError(lineNumber, result.kind, result.message)
            }
        }

        val stats = ProcessingStats(dataLines.size, products.size, errors)
        stats.print()

        saver.save(products, outputPath)
        println("\nСохранено в '$outputPath' (формат: ${saver.formatName})")
    }
}
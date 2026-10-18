package stats

import model.ErrorKind
import model.ParseError

data class ProcessingStats(
    val totalLines: Int,
    val loaded: Int,
    val errors: List<ParseError>
) {
    val parsingErrors: Int get() = errors.count { it.kind == ErrorKind.PARSING }
    val validationErrors: Int get() = errors.count { it.kind == ErrorKind.VALIDATION }

    fun print() {
        println("\nСтатистика:")
        println("""
            Обработано строк: $totalLines
            Успешно загружено товаров: $loaded
            Ошибок парсинга: $parsingErrors
            Ошибок валидации: $validationErrors
        """.trimIndent())
        if (errors.isNotEmpty()) {
            println("\nОтчёт об ошибках:")
            errors.forEach { println("[ОШИБКА] Строка ${it.lineNumber}: ${it.message}") }
        }
    }
}
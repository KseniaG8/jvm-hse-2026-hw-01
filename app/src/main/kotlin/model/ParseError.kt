package model

enum class ErrorKind { PARSING, VALIDATION }

data class ParseError(
    val lineNumber: Int,
    val kind: ErrorKind,
    val message: String
)
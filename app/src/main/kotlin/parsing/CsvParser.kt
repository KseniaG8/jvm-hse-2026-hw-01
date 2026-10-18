package parsing

object CsvParser {

    fun parseLine(line: String): List<String> {
        val fields = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                inQuotes && c == '"' && i + 1 < line.length && line[i + 1] == '"' -> {
                    sb.append('"'); i++
                }
                c == '"' -> inQuotes = !inQuotes
                c == ',' && !inQuotes -> {
                    fields.add(sb.toString()); sb.clear()
                }
                else -> sb.append(c)
            }
            i++
        }
        fields.add(sb.toString())
        return fields
    }
}
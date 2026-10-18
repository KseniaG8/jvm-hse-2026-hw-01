package parsing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CsvParserTest {
    @Test fun `plain line`() {
        assertEquals(listOf("1","abc","oil"), CsvParser.parseLine("1,abc,oil"))
    }
    @Test fun `quoted commas`() {
        assertEquals(listOf("1","Mobil 1, Synthetic","oil"), CsvParser.parseLine("""1,"Mobil 1, Synthetic",oil"""))
    }
    @Test fun `empty fields`() {
        assertEquals(listOf("1","","oil",""), CsvParser.parseLine("1,,oil,"))
    }
}
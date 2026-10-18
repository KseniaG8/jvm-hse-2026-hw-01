package saving

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class SaverGenerationTest {
    private val sample = listOf(
        model.Product.Oil(1,"Mobil","1125.0".toDouble(),"4.54".toDouble(), model.Vehicle.PASSENGER, 4.0)
    )
    private fun tmp() = Files.createTempFile("test", ".out").toFile()

    @Test fun `json produced`() {
        val f = tmp(); JsonSaver().save(sample, f.path)
        val text = File(f.path).readText()
        assertTrue(text.startsWith("[") && text.contains("\"id\": 1"))
    }
    @Test fun `xml produced`() {
        val f = tmp(); XmlSaver().save(sample, f.path)
        assertTrue(File(f.path).readText().contains("<id>1</id>"))
    }
    @Test fun `text produced`() {
        val f = tmp(); TextReportSaver().save(sample, f.path)
        assertTrue(File(f.path).readText().contains("Mobil"))
    }
}
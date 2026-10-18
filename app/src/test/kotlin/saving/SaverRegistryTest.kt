package saving

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SaverRegistryTest {
    @Test fun `default registry contains three formats`() {
        assertEquals(setOf("json","xml","txt"), SaverRegistry.default().availableFormats())
    }
    @Test fun `can register new saver`() {
        val reg = SaverRegistry().apply { register(object : DataSaver {
            override val formatName = "csv"; override fun save(products: List<model.Product>, path: String) {}
        }) }
        assertNotNull(reg.get("csv"))
    }
}
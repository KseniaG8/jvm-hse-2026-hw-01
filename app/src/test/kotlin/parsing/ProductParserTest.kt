package parsing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parsing.*
import saving.*
import validation.ProductValidator
import java.io.File
import java.nio.file.Files

class ProductParserTest {
    @Test fun `usd to rub`() {
        val r = ProductParser.parse(listOf("1","Mobil 1, Synthetic","oil", "$12.5","1kg","PASSENGER","volume=4"))
        assertEquals(1125.0, (r as ParseResult.Success).product.priceRub, 0.001)
    }
    @Test fun `lb to kg`() {
        val r = ProductParser.parse(listOf("5","Щетки стеклоочистителя","accessory","100руб","10lb","PASSENGER"))
        assertEquals(4.54, (r as ParseResult.Success).product.weightKg, 0.001)
    }
}
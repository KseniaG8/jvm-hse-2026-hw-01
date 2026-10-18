package validation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parsing.*

class ProductValidatorTest {
    @Test fun `negative price rejected`() {
        val r = ProductParser.parse(listOf("10","Шина","tire","-5$","0.3kg","PASSENGER","diameter=17"))
        assertTrue(r is ParseResult.Failure && (r as ParseResult.Failure).kind == model.ErrorKind.VALIDATION)
    }
    @Test fun `invalid color rejected`() {
        val r = ProductParser.parse(listOf("9","Присадка","accessory","50руб","0.1kg","PASSENGER","color=red"))
        assertTrue(r is ParseResult.Failure)
    }
    @Test fun `valid accessory accepted`() {
        val r = ProductParser.parse(listOf("6","Bosch, Air Filter","accessory","7.5","0.05kg","MOTORCYCLE","material=бумага","color=белый"))
        assertTrue(r is ParseResult.Success)
    }
    @Test fun `empty name rejected`() {
        val r = ProductParser.parse(listOf("8","","tire","10$","0.2","PASSENGER","diameter=16"))
        assertTrue(r is ParseResult.Failure)
    }
}
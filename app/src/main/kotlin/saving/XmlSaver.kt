package saving

import model.Product
import java.io.File

class XmlSaver : DataSaver {
    override val formatName = "xml"
    override fun save(products: List<Product>, path: String) {
        val sb = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<products>\n")
        products.forEach { p ->
            sb.append("  <product>\n")
            sb.append("    <id>${p.id}</id>\n")
            sb.append("    <name>${p.name.xmlEscape()}</name>\n")
            sb.append("    <type>${p.typeName()}</type>\n")
            sb.append("    <priceRub>${p.priceRub}</priceRub>\n")
            sb.append("    <weightKg>${p.weightKg}</weightKg>\n")
            sb.append("    <vehicle>${p.vehicle}</vehicle>\n")
            sb.append("    <extra>\n")
            p.extraFields().forEach { (k, v) ->
                sb.append("      <field key=\"$k\">${v.toString().xmlEscape()}</field>\n")
            }
            sb.append("    </extra>\n")
            sb.append("  </product>\n")
        }
        sb.append("</products>")
        File(path).writeText(sb.toString())
    }
}

private fun String.xmlEscape() = replace("&", "&amp;").replace("<", "&lt;")
    .replace(">", "&gt;").replace("\"", "&quot;")
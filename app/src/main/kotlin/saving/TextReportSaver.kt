package saving

import model.Product
import java.io.File

class TextReportSaver : DataSaver {
    override val formatName = "txt"
    override fun save(products: List<Product>, path: String) {
        val sb = StringBuilder("REPORT ON GOODS\n================\n")
        products.forEach { p ->
            sb.append("#${p.id} ${p.name} [${p.typeName()}]\n")
            sb.append("  Price: %.2f руб.\n".format(p.priceRub))
            sb.append("  Weight: %.3f кг\n".format(p.weightKg))
            sb.append("  Vehicle: ${p.vehicle}\n")
            p.extraFields().forEach { (k, v) -> sb.append("  $k: $v\n") }
            sb.append("-----\n")
        }
        File(path).writeText(sb.toString())
    }
}
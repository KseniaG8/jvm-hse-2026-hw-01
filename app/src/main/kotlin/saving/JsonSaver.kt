package saving

import model.Product
import java.io.File

class JsonSaver : DataSaver {
    override val formatName = "json"
    override fun save(products: List<Product>, path: String) {
        val sb = StringBuilder("[\n")
        products.forEachIndexed { i, p ->
            sb.append("  {\n")
            sb.append("""    "id": ${p.id},${'\n'}""")
            sb.append("""    "name": "${p.name.escape()}",${'\n'}""")
            sb.append("""    "type": "${p.typeName()}",${'\n'}""")
            sb.append("""    "priceRub": ${p.priceRub},${'\n'}""")
            sb.append("""    "weightKg": ${p.weightKg},${'\n'}""")
            sb.append("""    "vehicle": "${p.vehicle}"""")
            val extras = p.extraFields()
            if (extras.isNotEmpty()) {
                sb.append(",\n")
                sb.append(extras.entries.joinToString(",\n") { (k, v) ->
                    """    "$k": "${v.toString().escape()}""""
                })
            }
            sb.append("\n  }")
            if (i < products.lastIndex) sb.append(",")
            sb.append("\n")
        }
        sb.append("]")
        File(path).writeText(sb.toString())
    }
}

fun Product.typeName() = when (this) {
    is Product.Oil -> "oil"
    is Product.Tire -> "tire"
    is Product.Accessory -> "accessory"
}

fun Product.extraFields(): Map<String, Any?> = when (this) {
    is Product.Oil -> mapOf("volume" to volumeLiters)
    is Product.Tire -> mapOf("diameter" to diameterInch, "season" to season)
    is Product.Accessory -> mapOf("material" to material, "color" to color)
}.filterValues { it != null }

fun String.escape(): String = replace("\\", "\\\\").replace("\"", "\\\"")
package parsing

import model.*
import validation.ProductValidator

sealed class ParseResult {
    data class Success(val product: Product) : ParseResult()
    data class Failure(val kind: ErrorKind, val message: String) : ParseResult()
}

object ProductParser {

    private const val USD_RATE = 90.0
    private const val LB_TO_KG = 0.454

    fun parse(fields: List<String>): ParseResult {
        if (fields.size < 6) return fail("Недостаточно колонок (ожидается минимум 6)")
        val idRaw      = fields[0]
        val name       = fields[1]
        val typeRaw    = fields[2]
        val priceRaw   = fields[3]
        val weightRaw  = fields[4]
        val vehicleRaw = fields[5]
        val extraRaw = fields.getOrElse(6) { "" }
        val type = typeRaw.trim().lowercase()

        val id = idRaw.trim().toIntOrNull()
            ?: return fail("Некорректный id '$idRaw'")

        ProductValidator.validateType(type)?.let { return fail(it) }

        val priceRub = parsePrice(priceRaw.trim())
            ?: return parseFail("Не удалось распарсить цену '$priceRaw'")
        ProductValidator.validatePrice(priceRub)?.let { return fail(it) }

        val weightKg = parseWeight(weightRaw.trim())
            ?: return parseFail("Не удалось распарсить вес '$weightRaw'")
        ProductValidator.validateWeight(weightKg)?.let { return fail(it) }

        val vehicle = ProductValidator.parseVehicle(vehicleRaw.trim())
            ?: return parseFail("Неизвестный тип автомобиля '$vehicleRaw'")

        ProductValidator.validateName(name)?.let { return fail(it) }

        val extra = parseExtra(extraRaw)
        return when (type) {
            "oil"       -> buildOil(id, name, priceRub, weightKg, vehicle, extra)
            "tire"      -> buildTire(id, name, priceRub, weightKg, vehicle, extra)
            "accessory" -> buildAccessory(id, name, priceRub, weightKg, vehicle, extra)
            else -> fail("Неизвестный тип товара '$type'")
        }
    }

    private fun parsePrice(raw: String): Double? {
        val normalized = raw.replace(" ", "")
        return when {
            normalized.startsWith("$") ->
                normalized.removePrefix("$").toDoubleOrNull()?.times(USD_RATE)
            normalized.endsWith("$") ->
                normalized.removeSuffix("$").toDoubleOrNull()?.times(USD_RATE)
            normalized.endsWith("руб") ->
                normalized.removeSuffix("руб").toDoubleOrNull()
            else -> normalized.toDoubleOrNull()
        }
    }

    private fun parseWeight(raw: String): Double? {
        val normalized = raw.replace(" ", "").lowercase()
        return when {
            normalized.endsWith("lb") -> normalized.removeSuffix("lb").toDoubleOrNull()?.times(LB_TO_KG)
            normalized.endsWith("kg") -> normalized.removeSuffix("kg").toDoubleOrNull()
            else -> normalized.toDoubleOrNull()
        }
    }

    fun parseExtra(raw: String): Map<String, String> =
        raw.split(";")
            .mapNotNull { pair ->
                val idx = pair.indexOf('=')
                if (idx <= 0) null
                else pair.substring(0, idx).trim() to pair.substring(idx + 1).trim()
            }
            .toMap()

    private fun buildOil(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: Vehicle, extra: Map<String, String>
    ): ParseResult {
        val volumeRaw = extra["volume"]
        val volume = if (volumeRaw.isNullOrBlank()) null
        else volumeRaw.toDoubleOrNull() ?: return parseFail("Объем '$volumeRaw' не является числом")
        return ParseResult.Success(Product.Oil(id, name, price, weight, vehicle, volume))
    }

    private fun buildTire(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: Vehicle, extra: Map<String, String>
    ): ParseResult {
        val diameterRaw = extra["diameter"]
        val diameter = if (diameterRaw.isNullOrBlank()) null
        else diameterRaw.toDoubleOrNull() ?: return parseFail("Диаметр '$diameterRaw' не является числом")
        val season = extra["season"]?.let { ProductValidator.parseSeason(it) ?: return parseFail("Неизвестный сезон '$it'") }
        return ParseResult.Success(Product.Tire(id, name, price, weight, vehicle, diameter, season))
    }

    private fun buildAccessory(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: Vehicle, extra: Map<String, String>
    ): ParseResult {
        val color = extra["color"]
        if (color != null) {
            ProductValidator.validateColor(color)?.let { return fail(it) }
        }
        return ParseResult.Success(Product.Accessory(id, name, price, weight, vehicle, extra["material"], color))
    }

    private fun fail(msg: String) = ParseResult.Failure(ErrorKind.VALIDATION, msg)
    private fun parseFail(msg: String) = ParseResult.Failure(ErrorKind.PARSING, msg)
}
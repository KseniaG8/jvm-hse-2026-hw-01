package validation

import model.Season
import model.Vehicle

object ProductValidator {

    val ALLOWED_COLORS = setOf("черный", "белый", "серебристый")

    val ALLOWED_EXTRA_KEYS: Map<String, Set<String>> = mapOf(
        "oil"       to setOf("volume"),
        "tire"      to setOf("diameter", "season"),
        "accessory" to setOf("material", "color")
    )

    fun validateName(name: String): String? =
        if (name.isBlank()) "Пустое название товара" else null

    fun validatePrice(price: Double): String? =
        if (price <= 0) "Цена не может быть отрицательной или нулевой" else null

    fun validateWeight(weight: Double): String? =
        if (weight <= 0) "Вес не может быть отрицательным или нулевым" else null

    fun validateType(type: String): String? =
        if (type !in ALLOWED_EXTRA_KEYS) "Неизвестный тип товара '$type'" else null

    fun parseVehicle(raw: String): Vehicle? =
        Vehicle.entries.firstOrNull { it.name.equals(raw, ignoreCase = true) }

    fun parseSeason(raw: String): Season? =
        Season.entries.firstOrNull { it.name.equals(raw, ignoreCase = true) }

    fun validateColor(color: String): String? =
        if (color !in ALLOWED_COLORS) "Недопустимое значение 'color=$color'" else null
}
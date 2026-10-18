package model

sealed class Product {
    abstract val id : Int
    abstract val name : String
    abstract val priceRub : Double
    abstract val weightKg : Double
    abstract val vehicle : Vehicle

    data class Oil(
        override val id : Int,
        override val name : String,
        override val priceRub : Double,
        override val weightKg : Double,
        override val vehicle : Vehicle,
        val volumeLiters : Double?
    ) : Product()

    data class Tire(
        override val id: Int,
        override val name: String,
        override val priceRub: Double,
        override val weightKg: Double,
        override val vehicle: Vehicle,
        val diameterInch: Double?,
        val season: Season?
    ) : Product()

    data class Accessory(
        override val id: Int,
        override val name: String,
        override val priceRub: Double,
        override val weightKg: Double,
        override val vehicle: Vehicle,
        val material: String?,
        val color: String?
    ) : Product()
}

enum class Vehicle { PASSENGER, TRUCK, MOTORCYCLE }
enum class Season { SUMMER, WINTER, ALL_SEASON }
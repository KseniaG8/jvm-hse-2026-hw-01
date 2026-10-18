package saving

import model.Product

interface DataSaver {
    val formatName: String
    fun save(products: List<Product>, path: String)
}
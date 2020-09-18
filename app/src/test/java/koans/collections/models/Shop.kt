package koans.collections.models

data class Shop(val name: String, val customers: List<Customer>)

data class Customer(val name: String, val city: City, val orders: List<Order>)

data class Order(val product: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double) {
    override fun toString(): String = "'$name' for $price"
}

data class City(val name: String) {
    override fun toString() = name
}
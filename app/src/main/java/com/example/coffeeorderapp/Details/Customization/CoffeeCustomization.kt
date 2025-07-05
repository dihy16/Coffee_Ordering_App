package com.example.coffeeorderapp.Details.Customization

data class CoffeeCustomization(
    val quantity: Int = 1,
    val shot: ShotType = ShotType.Single,
    val select: SelectType = SelectType.Hot,
    val size: SizeType = SizeType.Medium,
    val ice: IceAmount = IceAmount.None
)

enum class ShotType { Single, Double }
enum class SelectType { Hot, Cold }
enum class SizeType { Small, Medium, Large }
enum class IceAmount { None, Some, A_Lot }

data class CartItem(val coffee: com.example.coffeeorderapp.HomePage.Data.Coffee, val customization: CoffeeCustomization, val totalPrice: Double)

object CartRepository {
    private val cartItems = mutableListOf<CartItem>()
    fun addItem(item: CartItem) { 
        android.util.Log.d("CartRepository", "Adding item: ${item.coffee.name}, price: ${item.totalPrice}")
        cartItems.add(item) 
        android.util.Log.d("CartRepository", "Total items in cart: ${cartItems.size}")
    }
    fun getItems(): List<CartItem> = cartItems.toList()
    fun clear() { cartItems.clear() }
}
package com.example.coffeeorderapp.cart.data

import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.Details.Customization.CoffeeCustomization
import com.example.coffeeorderapp.Details.Customization.ShotType
import com.example.coffeeorderapp.Details.Customization.SelectType
import com.example.coffeeorderapp.Details.Customization.SizeType
import com.example.coffeeorderapp.Details.Customization.IceAmount
import com.example.coffeeorderapp.HomePage.Data.Coffee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val cartDao: CartDao) {

    suspend fun getAllItems(): List<CartItem> = withContext(Dispatchers.IO) {
        cartDao.getAll().map { it.toCartItem() }
    }

    suspend fun addItem(item: CartItem) = withContext(Dispatchers.IO) {
        cartDao.insert(item.toEntity())
    }

    suspend fun removeItem(item: CartItem) = withContext(Dispatchers.IO) {
        cartDao.delete(item.toEntity())
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clear()
    }

    // Extension functions for conversion
    private fun CartItem.toEntity(): CartEntity = CartEntity(
        id = id,
        coffeeName = coffee.name,
        imageResId = coffee.imageResId,
        price = totalPrice,
        quantity = customization.quantity,
        shot = customization.shot.name,
        select = customization.select.name,
        size = customization.size.name,
        ice = customization.ice.name
    )

    private fun CartEntity.toCartItem(): CartItem = CartItem(
        id = id,
        coffee = Coffee(coffeeName, imageResId, price),
        customization = CoffeeCustomization(
            quantity = quantity,
            shot = ShotType.valueOf(shot),
            select = SelectType.valueOf(select),
            size = SizeType.valueOf(size),
            ice = IceAmount.valueOf(ice)
        ),
        totalPrice = price
    )
} 
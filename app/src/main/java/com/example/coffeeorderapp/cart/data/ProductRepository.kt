package com.example.coffeeorderapp.cart.data

class ProductRepository(private val productDao: ProductDao) {
    suspend fun getAllProducts() = productDao.getAllProducts()
    suspend fun getThreeCheapestProducts() = productDao.getThreeCheapestProducts()
    suspend fun getProductCount() = productDao.getProductCount()
    suspend fun deleteAllProducts() = productDao.deleteAll()
} 
package com.example.coffeeorderapp.cart.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Utility class to handle initial data seeding and initialization
 */
object DataInitializer {
    
    /**
     * Initialize all required application data
     * This should be called when the app starts to ensure all necessary data is seeded
     */
    fun initializeAppData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseModule.checkAndSeedInitialData(context)
        }
    }
    
    /**
     * Reset all data to initial state (useful for testing or debugging)
     */
    fun resetAppData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseModule.resetToInitialState(context)
        }
    }
    
    /**
     * Seed sample cart data for testing purposes
     */
    fun seedSampleCartData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseModule.seedSampleData(context)
        }
    }

    /**
     * Seed sample order history for testing purposes
     */
    fun seedSampleOrderHistory(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseModule.seedSampleOrderHistory(context)
        }
    }

    /**
     * Initialize app with sample data for testing
     */
    fun initializeWithSampleData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseModule.checkAndSeedInitialData(context)
            DatabaseModule.seedSampleData(context)
            DatabaseModule.seedSampleOrderHistory(context)
        }
    }

    /**
     * Force re-seed profile data (useful for debugging)
     */
    fun forceReseedProfile(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = DatabaseModule.getDatabase(context)
            val profileDao = database.profileDao()
            
            // Clear existing profile
            profileDao.deleteAll()
            
            // Insert new profile
            val initialProfile = com.example.coffeeorderapp.Profile.Model.ProfileEntity(
                id = 1,
                fullName = "Anderson",
                phoneNumber = "+60134589525",
                email = "Anderson@email.com",
                address = "3 Addersion Court\nChino Hills, HO56824, United State"
            )
            profileDao.insert(initialProfile)
            println("DEBUG: Force re-seeded profile: $initialProfile")
        }
    }

    /**
     * Force re-seed products data (useful for debugging duplicates)
     */
    fun forceReseedProducts(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = DatabaseModule.getDatabase(context)
            val productDao = database.productDao()
            
            // Clear existing products
            productDao.deleteAll()
            println("DEBUG: Cleared all products")
            
            // Insert new products
            val initialProducts = listOf(
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Americano", 
                    imageResId = com.example.coffeeorderapp.R.drawable.americano, 
                    price = 2.75
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Flat White", 
                    imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, 
                    price = 3.00
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Cappuccino", 
                    imageResId = com.example.coffeeorderapp.R.drawable.cappucino, 
                    price = 3.25
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Mocha", 
                    imageResId = com.example.coffeeorderapp.R.drawable.mocha, 
                    price = 4.00
                )
            )
            productDao.insertAll(initialProducts)
            
            // Verify
            val count = productDao.getProductCount()
            val products = productDao.getAllProducts()
            val cheapest = productDao.getThreeCheapestProducts()
            println("DEBUG: Force re-seeded $count products: ${products.map { it.name }}")
            println("DEBUG: 3 cheapest for redeem: ${cheapest.map { it.name }}")
        }
    }

    /**
     * Initialize database synchronously (blocks until complete)
     */
    fun initializeDatabaseSynchronously(context: Context) {
        println("DEBUG: Starting synchronous database initialization")
        
        // Use runBlocking to make this synchronous
        kotlinx.coroutines.runBlocking(kotlinx.coroutines.Dispatchers.IO) {
            // Completely reset the database
            DatabaseModule.resetDatabase(context)
            
            // Wait a moment for database to be deleted
            kotlinx.coroutines.delay(100)
            
            // Re-initialize everything
            DatabaseModule.checkAndSeedInitialData(context)
            
            // Verify
            val database = DatabaseModule.getDatabase(context)
            val productCount = database.productDao().getProductCount()
            val products = database.productDao().getAllProducts()
            val cheapest = database.productDao().getThreeCheapestProducts()
            
            println("DEBUG: Synchronous initialization complete. Found $productCount products: ${products.map { it.name }}")
            println("DEBUG: 3 cheapest for redeem: ${cheapest.map { it.name }}")
            if (productCount > 4) {
                println("DEBUG: WARNING - Found $productCount products (more than expected 4)")
            } else {
                println("DEBUG: Correct number of products found")
            }
        }
    }

    /**
     * Clear and re-seed only products (without full database reset)
     */
    fun clearAndReseedProducts(context: Context) {
        println("DEBUG: Starting clear and re-seed products only")
        
        kotlinx.coroutines.runBlocking(kotlinx.coroutines.Dispatchers.IO) {
            val database = DatabaseModule.getDatabase(context)
            val productDao = database.productDao()
            
            // Check current state
            val beforeCount = productDao.getProductCount()
            val beforeProducts = productDao.getAllProducts()
            println("DEBUG: Before clearing - Found $beforeCount products: ${beforeProducts.map { it.name }}")
            
            // Clear all products
            productDao.deleteAll()
            val afterCount = productDao.getProductCount()
            println("DEBUG: After clearing - Found $afterCount products")
            
            // Insert exactly 4 products
            val initialProducts = listOf(
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Americano", 
                    imageResId = com.example.coffeeorderapp.R.drawable.americano, 
                    price = 2.75
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Flat White", 
                    imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, 
                    price = 3.00
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Cappuccino", 
                    imageResId = com.example.coffeeorderapp.R.drawable.cappucino, 
                    price = 3.25
                ),
                com.example.coffeeorderapp.cart.data.ProductEntity(
                    name = "Mocha", 
                    imageResId = com.example.coffeeorderapp.R.drawable.mocha, 
                    price = 4.00
                )
            )
            
            productDao.insertAll(initialProducts)
            
            // Final verification
            val finalCount = productDao.getProductCount()
            val finalProducts = productDao.getAllProducts()
            val cheapest = productDao.getThreeCheapestProducts()
            
            println("DEBUG: After re-seeding - Found $finalCount products: ${finalProducts.map { it.name }}")
            println("DEBUG: 3 cheapest for redeem: ${cheapest.map { it.name }}")
            
            if (finalCount == 4) {
                println("DEBUG: SUCCESS - Products cleared and re-seeded correctly")
            } else {
                println("DEBUG: ERROR - Expected 4 products but found $finalCount")
            }
        }
    }

    /**
     * Completely reset database and re-seed (nuclear option for debugging)
     */
    fun nuclearReset(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            println("DEBUG: Starting nuclear reset of database")
            
            // Completely reset the database
            DatabaseModule.resetDatabase(context)
            
            // Wait a moment for database to be deleted
            kotlinx.coroutines.delay(100)
            
            // Re-initialize everything
            DatabaseModule.checkAndSeedInitialData(context)
            
            // Verify
            val database = DatabaseModule.getDatabase(context)
            val productCount = database.productDao().getProductCount()
            val products = database.productDao().getAllProducts()
            val cheapest = database.productDao().getThreeCheapestProducts()
            
            println("DEBUG: Nuclear reset complete. Found $productCount products: ${products.map { it.name }}")
            println("DEBUG: 3 cheapest for redeem: ${cheapest.map { it.name }}")
            if (productCount > 4) {
                println("DEBUG: WARNING - Found $productCount products (more than expected 4)")
            } else {
                println("DEBUG: Correct number of products found")
            }
        }
    }
} 
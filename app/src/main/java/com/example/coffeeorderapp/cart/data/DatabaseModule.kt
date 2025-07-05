package com.example.coffeeorderapp.cart.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.coffeeorderapp.Orders.OrderDao
import com.example.coffeeorderapp.Orders.OrderEntity
import com.example.coffeeorderapp.Orders.OrderItemEntity
import com.example.coffeeorderapp.Orders.OrderRepository
import com.example.coffeeorderapp.Rewards.RewardStatusDao
import com.example.coffeeorderapp.Rewards.RewardStatusEntity
import com.example.coffeeorderapp.cart.data.ProductDao
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.cart.data.ProductRepository
import com.example.coffeeorderapp.Profile.data.ProfileDao
import com.example.coffeeorderapp.Profile.data.ProfileRepository
import com.example.coffeeorderapp.Profile.Model.ProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseModule {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "coffee_order_database"
            )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Seed initial data when database is first created
                    CoroutineScope(Dispatchers.IO).launch {
                        seedInitialData()
                    }
                }
            })
            .build()
        }
        return database!!
    }

    // Method to completely reset the database (for debugging)
    fun resetDatabase(context: Context) {
        println("DEBUG: Starting complete database reset")
        
        // Close and clear the database instance
        database?.close()
        database = null
        
        // Delete the database file
        val deleted = context.deleteDatabase("coffee_order_database")
        println("DEBUG: Database file deleted: $deleted")
        
        // Also try to delete any backup files
        val dbFile = context.getDatabasePath("coffee_order_database")
        val dbShmFile = context.getDatabasePath("coffee_order_database-shm")
        val dbWalFile = context.getDatabasePath("coffee_order_database-wal")
        
        if (dbFile.exists()) {
            dbFile.delete()
            println("DEBUG: Deleted main database file")
        }
        if (dbShmFile.exists()) {
            dbShmFile.delete()
            println("DEBUG: Deleted database shm file")
        }
        if (dbWalFile.exists()) {
            dbWalFile.delete()
            println("DEBUG: Deleted database wal file")
        }
        
        println("DEBUG: Complete database reset finished")
    }

    fun getCartDao(context: Context): CartDao {
        return getDatabase(context).cartDao()
    }

    fun getCartRepository(context: Context): CartRepository {
        return CartRepository(getCartDao(context))
    }

    fun getOrderDao(context: Context): OrderDao {
        return getDatabase(context).orderDao()
    }

    fun getOrderRepository(context: Context): OrderRepository {
        return OrderRepository(getDatabase(context))
    }

    fun getRewardStatusDao(context: Context): RewardStatusDao {
        return getDatabase(context).rewardStatusDao()
    }

    fun getProductDao(context: Context): ProductDao {
        return getDatabase(context).productDao()
    }

    fun getProductRepository(context: Context): ProductRepository {
        return ProductRepository(getProductDao(context))
    }

    fun getProfileDao(context: Context): ProfileDao {
        return getDatabase(context).profileDao()
    }

    fun getProfileRepository(context: Context): ProfileRepository {
        return ProfileRepository(getProfileDao(context))
    }

    private suspend fun seedInitialData() {
        println("DEBUG: Starting seedInitialData")
        
        // Clear existing products to avoid duplicates
        val productDao = database?.productDao()
        if (productDao != null) {
            val beforeCount = productDao.getProductCount()
            println("DEBUG: Found $beforeCount products before clearing")
            
            productDao.deleteAll()
            val afterCount = productDao.getProductCount()
            println("DEBUG: After clearing, found $afterCount products")
        }
        
        // Seed initial product data - only 4 coffees as requested
        val initialProducts = listOf(
            ProductEntity(name = "Americano", imageResId = com.example.coffeeorderapp.R.drawable.americano, price = 2.75),
            ProductEntity(name = "Flat White", imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, price = 3.00),
            ProductEntity(name = "Cappuccino", imageResId = com.example.coffeeorderapp.R.drawable.cappucino, price = 3.25),
            ProductEntity(name = "Mocha", imageResId = com.example.coffeeorderapp.R.drawable.mocha, price = 4.00)
        )
        
        println("DEBUG: Inserting ${initialProducts.size} products")
        productDao?.insertAll(initialProducts)
        
        // Verify the seeding
        val count = productDao?.getProductCount() ?: 0
        val products = productDao?.getAllProducts() ?: emptyList()
        val cheapest = productDao?.getThreeCheapestProducts() ?: emptyList()
        
        println("DEBUG: Final verification - Found $count products:")
        products.forEach { product ->
            println("DEBUG:   - ${product.name} ($${product.price})")
        }
        println("DEBUG: 3 cheapest for redeem: ${cheapest.map { it.name }}")
        
        if (count != 4) {
            println("DEBUG: ERROR - Expected 4 products but found $count")
        } else {
            println("DEBUG: SUCCESS - Correct number of products seeded")
        }

        // Seed initial profile data
        val profileDao = database?.profileDao()
        val initialProfile = ProfileEntity(
            id = 1,
            fullName = "Anderson",
            phoneNumber = "+60134589525",
            email = "Anderson@email.com",
            address = "3 Addersion Court\nChino Hills, HO56824, United State"
        )
        profileDao?.insert(initialProfile)
        println("DEBUG: Profile seeded with data: $initialProfile")

        // Seed initial reward status data
        val rewardStatusDao = database?.rewardStatusDao()
        val initialRewardStatus = RewardStatusEntity(
            id = 1,
            loyaltyStamps = 0,
            totalPoints = 0
        )
        rewardStatusDao?.insertOrUpdate(initialRewardStatus)
    }

    // Public method to seed sample data for testing - using only the 4 specified coffees
    suspend fun seedSampleData(context: Context) {
        val cartDao = getCartDao(context)
        val sampleItems = listOf(
            CartEntity(
                coffeeName = "Americano",
                imageResId = com.example.coffeeorderapp.R.drawable.americano,
                price = 2.75,
                quantity = 1,
                shot = "Single",
                select = "Hot",
                size = "Medium",
                ice = "None"
            ),
            CartEntity(
                coffeeName = "Flat White",
                imageResId = com.example.coffeeorderapp.R.drawable.flatwhite,
                price = 3.00,
                quantity = 1,
                shot = "Single",
                select = "Hot",
                size = "Medium",
                ice = "None"
            ),
            CartEntity(
                coffeeName = "Cappuccino",
                imageResId = com.example.coffeeorderapp.R.drawable.cappucino,
                price = 3.25,
                quantity = 1,
                shot = "Single",
                select = "Hot",
                size = "Large",
                ice = "None"
            )
        )
        sampleItems.forEach { cartDao.insert(it) }
        println("DEBUG: Seeded sample cart with 3 items: Americano, Flat White, Cappuccino")
    }

    // Seed sample order history for testing
    suspend fun seedSampleOrderHistory(context: Context) {
        val database = getDatabase(context)
        val orderDao = database.orderDao()
        val orderItemDao = database.orderItemDao()

        // Sample orders - updated to match the 4 coffee prices
        val sampleOrders = listOf(
            OrderEntity(
                date = "2024-01-15",
                address = "3 Addersion Court, Chino Hills, HO56824, United State",
                status = "history",
                totalPrice = 9.00  // Americano + Flat White + Cappuccino
            ),
            OrderEntity(
                date = "2024-01-10",
                address = "3 Addersion Court, Chino Hills, HO56824, United State",
                status = "history",
                totalPrice = 10.00  // Mocha + Cappuccino + Americano
            )
        )

        sampleOrders.forEach { order ->
            val orderId = orderDao.insert(order).toInt()
            
            // Sample order items for each order - using only the 4 specified coffees
            val orderItems = when (order.totalPrice) {
                9.00 -> listOf(
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Americano",
                        imageResId = com.example.coffeeorderapp.R.drawable.americano,
                        price = 2.75,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Medium",
                        ice = "None"
                    ),
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Flat White",
                        imageResId = com.example.coffeeorderapp.R.drawable.flatwhite,
                        price = 3.00,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Medium",
                        ice = "None"
                    ),
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Cappuccino",
                        imageResId = com.example.coffeeorderapp.R.drawable.cappucino,
                        price = 3.25,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Small",
                        ice = "None"
                    )
                )
                10.00 -> listOf(
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Mocha",
                        imageResId = com.example.coffeeorderapp.R.drawable.mocha,
                        price = 4.00,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Large",
                        ice = "None"
                    ),
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Cappuccino",
                        imageResId = com.example.coffeeorderapp.R.drawable.cappucino,
                        price = 3.25,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Medium",
                        ice = "None"
                    ),
                    OrderItemEntity(
                        orderId = orderId,
                        coffeeName = "Americano",
                        imageResId = com.example.coffeeorderapp.R.drawable.americano,
                        price = 2.75,
                        quantity = 1,
                        shot = "Single",
                        select = "Hot",
                        size = "Large",
                        ice = "None"
                    )
                )
                else -> emptyList()
            }
            
            orderItems.forEach { orderItemDao.insert(it) }
        }
    }

    // Check if initial data needs to be seeded
    suspend fun checkAndSeedInitialData(context: Context) {
        val database = getDatabase(context)
        
        // Check if products exist and clear if there are duplicates
        val products = database.productDao().getAllProducts()
        val productCount = database.productDao().getProductCount()
        println("DEBUG: Found $productCount products in database")
        
        if (products.isEmpty()) {
            val initialProducts = listOf(
                ProductEntity(name = "Americano", imageResId = com.example.coffeeorderapp.R.drawable.americano, price = 2.75),
                ProductEntity(name = "Flat White", imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, price = 3.00),
                ProductEntity(name = "Cappuccino", imageResId = com.example.coffeeorderapp.R.drawable.cappucino, price = 3.25),
                ProductEntity(name = "Mocha", imageResId = com.example.coffeeorderapp.R.drawable.mocha, price = 4.00)
            )
            database.productDao().insertAll(initialProducts)
            println("DEBUG: Checked and seeded 4 coffees: Americano ($2.75), Flat White ($3.00), Cappuccino ($3.25), Mocha ($4.00)")
        } else if (productCount > 4) {
            // Clear and re-seed if there are duplicates
            println("DEBUG: Found $productCount products (more than 4), clearing and re-seeding")
            database.productDao().deleteAll()
            val initialProducts = listOf(
                ProductEntity(name = "Americano", imageResId = com.example.coffeeorderapp.R.drawable.americano, price = 2.75),
                ProductEntity(name = "Flat White", imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, price = 3.00),
                ProductEntity(name = "Cappuccino", imageResId = com.example.coffeeorderapp.R.drawable.cappucino, price = 3.25),
                ProductEntity(name = "Mocha", imageResId = com.example.coffeeorderapp.R.drawable.mocha, price = 4.00)
            )
            database.productDao().insertAll(initialProducts)
            println("DEBUG: Re-seeded 4 coffees after clearing duplicates")
        }

        // Check if profile exists
        val profile = database.profileDao().getProfile()
        println("DEBUG: Retrieved profile: $profile")
        if (profile == null) {
            val initialProfile = ProfileEntity(
                id = 1,
                fullName = "Anderson",
                phoneNumber = "+60134589525",
                email = "Anderson@email.com",
                address = "3 Addersion Court\nChino Hills, HO56824, United State"
            )
            database.profileDao().insert(initialProfile)
            println("DEBUG: Profile inserted: $initialProfile")
        }

        // Check if reward status exists
        val rewardStatus = database.rewardStatusDao().getStatus()
        if (rewardStatus == null) {
            val initialRewardStatus = RewardStatusEntity(
                id = 1,
                loyaltyStamps = 0,
                totalPoints = 0
            )
            database.rewardStatusDao().insertOrUpdate(initialRewardStatus)
        }
    }

    // Reset all data to initial state (for testing/debugging)
    suspend fun resetToInitialState(context: Context) {
        val database = getDatabase(context)
        
        // Clear all data
        database.cartDao().clear()
        database.orderDao().deleteAll()
        database.orderItemDao().deleteAll()
        
        // Re-seed initial data
        seedInitialData()
    }
} 
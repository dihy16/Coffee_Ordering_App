package com.example.coffeeorderapp.cart.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.coffeeorderapp.Orders.OrderDao
import com.example.coffeeorderapp.Orders.OrderRepository
import com.example.coffeeorderapp.Rewards.RewardStatusDao
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
        // Seed initial product data
        val productDao = database?.productDao()
        val initialProducts = listOf(
            ProductEntity(name = "Flat White", imageResId = com.example.coffeeorderapp.R.drawable.flatwhite, price = 3.00),
            ProductEntity(name = "Cappuccino", imageResId = com.example.coffeeorderapp.R.drawable.cappucino, price = 3.25),
            ProductEntity(name = "Americano", imageResId = com.example.coffeeorderapp.R.drawable.americano, price = 2.75),
            ProductEntity(name = "Mocha", imageResId = com.example.coffeeorderapp.R.drawable.mocha, price = 4.00)
        )
        productDao?.insertAll(initialProducts)

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
    }

    // Public method to seed sample data for testing
    suspend fun seedSampleData(context: Context) {
        val cartDao = getCartDao(context)
        val sampleItems = listOf(
            CartEntity(
                coffeeName = "Americano",
                imageResId = com.example.coffeeorderapp.R.drawable.americano,
                price = 3.50,
                quantity = 1,
                shot = "Single",
                select = "Hot",
                size = "Medium",
                ice = "None"
            ),
            CartEntity(
                coffeeName = "Cappuccino",
                imageResId = com.example.coffeeorderapp.R.drawable.cappucino,
                price = 4.25,
                quantity = 2,
                shot = "Double",
                select = "Hot",
                size = "Large",
                ice = "None"
            )
        )
        sampleItems.forEach { cartDao.insert(it) }
    }
} 
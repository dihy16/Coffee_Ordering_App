package com.example.coffeeorderapp.cart.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.coffeeorderapp.Orders.OrderDao
import com.example.coffeeorderapp.Orders.OrderRepository
import com.example.coffeeorderapp.Rewards.RewardStatusDao
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

    private suspend fun seedInitialData() {
        // You can add initial data here if needed
        // For example, if you want to pre-populate with some coffee items
        // This is optional - you can leave it empty if you don't need initial data
        
        // Example of seeding initial data:
        // val cartDao = getCartDao(context)
        // val initialItems = listOf(
        //     CartEntity(coffeeName = "Sample Coffee", imageResId = R.drawable.americano, price = 3.50, ...)
        // )
        // initialItems.forEach { cartDao.insert(it) }
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
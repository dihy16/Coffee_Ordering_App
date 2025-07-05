# Data Seeding Implementation

## Overview
This document describes the data seeding implementation for the Coffee Order App. The app automatically seeds required initial data when the database is first created and provides utilities for testing and debugging.

## Entities That Are Seeded

### 1. Products (ProductEntity)
- **Purpose**: Coffee products available for purchase
- **Seeded Data**:
  - Americano ($2.75) - Cheapest option
  - Flat White ($3.00)
  - Cappuccino ($3.25)
  - Mocha ($4.00) - Most expensive option
- **Usage**: Home page coffee list, cart functionality, redeem functionality
- **Redeem Options**: The 3 cheapest coffees (Americano, Flat White, Cappuccino) are available for redemption

### 2. User Profile (ProfileEntity)
- **Purpose**: User's personal information
- **Seeded Data**:
  - Name: "Anderson"
  - Phone: "+60134589525"
  - Email: "Anderson@email.com"
  - Address: "3 Addersion Court\nChino Hills, HO56824, United State"
- **Usage**: Profile screen, order delivery

### 3. Reward Status (RewardStatusEntity)
- **Purpose**: User's loyalty stamps and points
- **Seeded Data**:
  - Loyalty Stamps: 0
  - Total Points: 0
- **Usage**: Rewards screen, loyalty card functionality

## Implementation Details

### DatabaseModule.kt
The main class responsible for database initialization and data seeding.

#### Key Methods:
- `seedInitialData()`: Called automatically when database is first created
- `checkAndSeedInitialData()`: Checks if data exists and seeds if missing
- `resetToInitialState()`: Clears all data and re-seeds initial data
- `seedSampleData()`: Adds sample cart items for testing
- `seedSampleOrderHistory()`: Adds sample order history for testing

### DataInitializer.kt
Utility class providing easy-to-use methods for data initialization.

#### Key Methods:
- `initializeAppData()`: Ensures all required data is seeded
- `resetAppData()`: Resets all data to initial state
- `seedSampleCartData()`: Adds sample cart items
- `seedSampleOrderHistory()`: Adds sample order history
- `initializeWithSampleData()`: Initializes app with all sample data

## Automatic Seeding

### Database Creation
When the database is first created, the `RoomDatabase.Callback` automatically calls `seedInitialData()` to populate:
- Products
- User Profile
- Reward Status

### App Startup
In `MainActivity.onCreate()`, `DataInitializer.initializeAppData()` is called to ensure all required data exists.

## Manual Seeding for Testing

### Sample Cart Data
```kotlin
DataInitializer.seedSampleCartData(context)
```
Adds sample items to the cart for testing the cart functionality.

### Sample Order History
```kotlin
DataInitializer.seedSampleOrderHistory(context)
```
Adds sample completed orders for testing the orders screen and rewards history.

### Complete Sample Data
```kotlin
DataInitializer.initializeWithSampleData(context)
```
Initializes the app with all sample data including cart items and order history.

## Data Reset

### Reset to Initial State
```kotlin
DataInitializer.resetAppData(context)
```
Clears all data and re-seeds only the required initial data (products, profile, reward status).

## Database Schema

### Tables
1. **products**: Coffee products available for purchase
2. **profile**: User profile information
3. **reward_status**: User's loyalty stamps and points
4. **cart**: Current cart items
5. **orders**: Order history
6. **order_items**: Individual items in each order

### Relationships
- Orders have a one-to-many relationship with OrderItems
- All other entities are independent

## Usage Examples

### In Activities/Fragments
```kotlin
// Initialize required data
DataInitializer.initializeAppData(context)

// Add sample data for testing
DataInitializer.initializeWithSampleData(context)

// Reset data
DataInitializer.resetAppData(context)
```

### In ViewModels
```kotlin
// Access seeded data through repositories
val products = productRepository.getAllProducts()
val profile = profileRepository.getProfile()
val rewardStatus = rewardsRepository.getStatus()
```

## Best Practices

1. **Always call `initializeAppData()` on app startup**
2. **Use sample data only for testing/debugging**
3. **Reset data carefully as it clears all user data**
4. **Check for existing data before seeding to avoid duplicates**
5. **Use the provided utility methods instead of direct database access**

## Troubleshooting

### Data Not Appearing
1. Check if `DataInitializer.initializeAppData()` is called
2. Verify database version and migration
3. Check for database creation callbacks

### Duplicate Data
1. Use `checkAndSeedInitialData()` instead of direct seeding
2. Clear database and reinstall app if needed

### Sample Data Issues
1. Ensure all required drawable resources exist
2. Check for proper entity relationships
3. Verify DAO methods are implemented correctly 
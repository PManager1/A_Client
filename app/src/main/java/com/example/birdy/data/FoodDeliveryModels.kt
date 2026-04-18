package com.example.birdy.data

// MARK: - Models

data class FoodCategory(
    val name: String,
    val emoji: String
)

data class DeliveryRestaurant(
    val name: String,
    val rating: Double,
    val reviews: String,
    val distance: String,
    val deliveryTime: String,
    val deliveryFee: String,
    val imagePlaceholder: Long = 0xFFBB86FC
)

// MARK: - Static Data

object FoodDeliveryData {

    val categories = listOf(
        FoodCategory("Fast Food", "🍟"),
        FoodCategory("Pizza", "🍕"),
        FoodCategory("Burgers", "🍔"),
        FoodCategory("Chicken", "🍗"),
        FoodCategory("Desserts", "🍰"),
        FoodCategory("Healthy", "🥗"),
        FoodCategory("Indian", "🍛"),
        FoodCategory("Chinese", "🥡"),
        FoodCategory("Pho", "🍜"),
        FoodCategory("Bubble Tea", "🧋"),
        FoodCategory("Mexican", "🌮"),
        FoodCategory("Korean", "🥘"),
        FoodCategory("Soup", "🍲"),
        FoodCategory("Sandwich", "🥪"),
        FoodCategory("Asian", "🥢"),
        FoodCategory("Halal", "🍖"),
        FoodCategory("Thai", "🍛"),
        FoodCategory("Salad", "🥙"),
        FoodCategory("Seafood", "🦐"),
        FoodCategory("Japanese", "🍣")
    )

    val section1Restaurants = listOf(
        DeliveryRestaurant("Taco Bell", 4.0, "6k+", "1.5 mi", "30 min", "$0"),
        DeliveryRestaurant("Chick-A-Licious", 4.2, "1k+", "2.1 mi", "25 min", "$0")
    )

    val section2Restaurants = listOf(
        DeliveryRestaurant("Taco Bell", 4.0, "6k+", "1.5 mi", "30 min", "$0"),
        DeliveryRestaurant("Chick-A-Licious", 4.2, "1k+", "2.1 mi", "25 min", "$0")
    )

    val section3Restaurants = listOf(
        DeliveryRestaurant("Taco Bell", 4.0, "6k+", "1.5 mi", "30 min", "$0"),
        DeliveryRestaurant("Chick-A-Licious", 4.2, "1k+", "2.1 mi", "25 min", "$0")
    )

    val section4Restaurants = listOf(
        DeliveryRestaurant("Taco Bell", 4.0, "6k+", "1.5 mi", "30 min", "$0"),
        DeliveryRestaurant("Chick-A-Licious", 4.2, "1k+", "2.1 mi", "25 min", "$0")
    )
}
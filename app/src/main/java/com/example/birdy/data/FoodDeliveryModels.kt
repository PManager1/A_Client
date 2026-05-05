package com.example.birdy.data

import android.content.Context
import org.json.JSONObject

// MARK: - Legacy Models (kept for backward compatibility)

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

// MARK: - JSON Data Models (match homefeed.json / iOS HomeFeedData)

data class HomeFeedData(
    val featuredBanners: List<FeaturedBanner>,
    val sections: List<FeedSection>
)

data class FeaturedBanner(
    val id: String,
    val title: String,
    val subtitle: String,
    val gradientColors: List<String>,
    val actionText: String,
    val imageUrl: String
)

data class FeedSection(
    val heading: String,
    val restaurants: List<FeedRestaurant>
)

data class FeedRestaurant(
    val id: String,
    val restaurantName: String,
    val logoURL: String,
    val images: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val distance: Double,
    val deliveryTime: Int,
    val deliveryFee: Double,
    val promoText: String,
    val isSponsored: Boolean,
    val foodItems: List<String>,
    val isNew: Boolean
) {
    /** Format review count: 6000 → "6k+", 1200 → "1.2k+", 500 → "500+" */
    val reviewsDisplay: String
        get() {
            return if (reviewCount >= 1000) {
                val formatted = reviewCount / 1000.0
                val stripped = if (formatted % 1.0 == 0.0) {
                    "${formatted.toInt()}"
                } else {
                    String.format("%.1f", formatted)
                }
                "${stripped}k+"
            } else {
                "${reviewCount}+"
            }
        }

    /** "1.5 mi" */
    val distanceDisplay: String get() = "${distance} mi"

    /** "30 min" */
    val deliveryTimeDisplay: String get() = "${deliveryTime} min"

    /** "$0.00" delivery fee */
    val deliveryFeeDisplay: String
        get() = if (deliveryFee == 0.0) "$0" else String.format("$%.2f", deliveryFee)

    /** First image or empty */
    val thumbnailImage: String get() = images.firstOrNull() ?: ""
}

// MARK: - Static Data (categories — hardcoded for now, will come from backend later)

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

    // MARK: - Load Home Feed from JSON
    fun loadHomeFeed(context: Context): HomeFeedData? {
        return try {
            val json = context.assets.open("homefeed.json")
                .bufferedReader()
                .use { it.readText() }
            parseHomeFeed(json)
        } catch (e: Exception) {
            println("❌ [FoodDeliveryData] Failed to load homefeed.json: ${e.message}")
            null
        }
    }

    private fun parseHomeFeed(json: String): HomeFeedData {
        val root = JSONObject(json)

        // Parse banners
        val bannersArray = root.getJSONArray("featured_banners")
        val banners = (0 until bannersArray.length()).map { i ->
            val b = bannersArray.getJSONObject(i)
            val colorsArray = b.optJSONArray("gradient_colors")
            val colors = if (colorsArray != null) {
                (0 until colorsArray.length()).map { colorsArray.getString(it) }
            } else emptyList()

            FeaturedBanner(
                id = b.optString("id"),
                title = b.optString("title"),
                subtitle = b.optString("subtitle"),
                gradientColors = colors,
                actionText = b.optString("action_text"),
                imageUrl = b.optString("image_url")
            )
        }

        // Parse sections
        val sectionsArray = root.getJSONArray("sections")
        val sections = (0 until sectionsArray.length()).map { i ->
            val s = sectionsArray.getJSONObject(i)
            val heading = s.optString("heading")
            val restaurantsArray = s.getJSONArray("restaurants")
            val restaurants = (0 until restaurantsArray.length()).map { j ->
                val r = restaurantsArray.getJSONObject(j)
                val imagesArray = r.optJSONArray("images")
                val images = if (imagesArray != null) {
                    (0 until imagesArray.length()).map { imagesArray.getString(it) }
                } else emptyList()

                val foodItemsArray = r.optJSONArray("foodItems")
                val foodItems = if (foodItemsArray != null) {
                    (0 until foodItemsArray.length()).map { foodItemsArray.getString(it) }
                } else emptyList()

                FeedRestaurant(
                    id = r.optString("id"),
                    restaurantName = r.optString("restaurantName"),
                    logoURL = r.optString("logoURL", ""),
                    images = images,
                    rating = r.optDouble("rating", 0.0),
                    reviewCount = r.optInt("reviewCount", 0),
                    distance = r.optDouble("distance", 0.0),
                    deliveryTime = r.optInt("deliveryTime", 30),
                    deliveryFee = r.optDouble("deliveryFee", 0.0),
                    promoText = r.optString("promoText", ""),
                    isSponsored = r.optBoolean("isSponsored", false),
                    foodItems = foodItems,
                    isNew = r.optBoolean("isNew", false)
                )
            }
            FeedSection(heading = heading, restaurants = restaurants)
        }

        return HomeFeedData(featuredBanners = banners, sections = sections)
    }

    // Legacy static sections (kept for fallback)
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
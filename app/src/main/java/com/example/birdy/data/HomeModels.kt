package com.example.birdy.data

/**
 * Data models for the Home screen, mirroring the iOS Home.swift models.
 */

// Matches: HomeServiceCategory in iOS
data class HomeServiceCategory(
    val id: String,
    val name: String,
    val icon: String // Material Icons icon name or system icon name
)

// Matches: HomeReorderItem in iOS
data class HomeReorderItem(
    val name: String,
    val description: String,
    val imageUrl: String
)

// Matches: HomePopularItem in iOS
data class HomePopularItem(
    val title: String,
    val subtitle: String,
    val rating: Int,
    val imageUrl: String // Using URL instead of local resource name for flexibility
)

/**
 * Static data provider for Home screen content.
 * In production, some of this data will come from the backend.
 */
object HomeData {

    val placeholderServices = listOf(
        "auto repair", "pet care", "nails at home", "flat tire fix",
        "plumber", "electrician", "women's hair", "haircut",
        "locksmith", "lawn care", "handyman"
    )

    val reorderItems = listOf(
        HomeReorderItem(
            name = "Jane",
            description = "Re-book Jane for dog walking",
            imageUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=500&auto=format&fit=crop"
        )
    )

    val popularItems = listOf(
        HomePopularItem(
            title = "Mobile Car Wash:",
            subtitle = "Get your car sparkling!",
            rating = 4,
            imageUrl = "https://images.unsplash.com/photo-1520340356584-f9166066d675?w=500&auto=format&fit=crop"
        ),
        HomePopularItem(
            title = "Outdoor Yoga:",
            subtitle = "Find your zen!",
            rating = 5,
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=500&auto=format&fit=crop"
        ),
        HomePopularItem(
            title = "Private Chef:",
            subtitle = "Gourmet meals at home",
            rating = 5,
            imageUrl = "https://images.unsplash.com/photo-1556910103-1c02745aae4d?w=500&auto=format&fit=crop"
        )
    )

    val trendingSearches = listOf(
        "Pumpkin Patches",
        "Soup",
        "Sports Bars",
        "Chimney Sweeps"
    )

    // Fallback categories (matches iOS loadFallbackCategories)
    val fallbackCategories = listOf(
        HomeServiceCategory(id = "electrical", name = "Electrical", icon = "bolt.fill"),
        HomeServiceCategory(id = "plumbing", name = "Plumbing", icon = "wrench.fill")
    )
}
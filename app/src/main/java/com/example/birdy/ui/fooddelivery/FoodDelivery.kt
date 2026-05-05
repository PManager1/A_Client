package com.example.birdy.ui.fooddelivery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.birdy.data.FoodDeliveryData
import com.example.birdy.data.HomeFeedData

/**
 * Food Delivery Screen — replicates iOS FoodDeliveryView.swift
 *
 * Loads data from homefeed.json (banners + dynamic sections)
 * Sections (top to bottom):
 * 1. Header (address pin + notification bell + cart)
 * 2. Search bar (disabled, navigates to search on tap)
 * 3. Category horizontal scroll (20 emoji categories)
 * 4. Featured banners (from JSON)
 * 5. Dynamic sections with restaurant cards (from JSON)
 */
@Composable
fun FoodDeliveryScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onRestaurantClick: (restaurantName: String) -> Unit = {},
    onCategoryClick: (categoryName: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Address state (stub — will connect to AddressService later)
    var selectedAddress by remember { mutableStateOf<String?>(null) }
    var selectedAddressId by remember { mutableStateOf<String?>(null) }
    var isLoadingAddress by remember { mutableStateOf(false) }
    var showAddressSheet by remember { mutableStateOf(false) }

    // JSON-driven data
    var homeFeed by remember { mutableStateOf<HomeFeedData?>(null) }

    // Load home feed from JSON on appear
    LaunchedEffect(Unit) {
        homeFeed = FoodDeliveryData.loadHomeFeed(context)
        if (homeFeed != null) {
            println("✅ [FoodDeliveryScreen] Loaded home feed: ${homeFeed!!.featuredBanners.size} banners, ${homeFeed!!.sections.size} sections")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // MARK: - Header
            FoodDeliveryHeader(
                selectedAddress = selectedAddress,
                isLoadingAddress = isLoadingAddress,
                onAddressClick = {
                    showAddressSheet = true
                },
                onCartClick = { onNavigateToCart() }
            )

            // MARK: - Search Bar
            FoodSearchBar(
                onClick = { onNavigateToSearch() },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Categories
            FoodCategoryList(
                categories = FoodDeliveryData.categories,
                onCategoryClick = { category ->
                    onCategoryClick(category.name)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Featured Banners (from JSON)
            homeFeed?.featuredBanners?.let { banners ->
                banners.forEach { banner ->
                    DynamicPromoBannerView(
                        banner = banner,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // MARK: - Dynamic Sections (from JSON)
                homeFeed?.sections?.forEach { section ->
                    FeedRestaurantSection(
                        title = section.heading,
                        restaurants = section.restaurants,
                        onRestaurantClick = { onRestaurantClick(it.restaurantName) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // MARK: - Address Selection Sheet
        if (showAddressSheet) {
            SelectAddressSheet(
                currentAddressId = selectedAddressId,
                onAddressSelected = { address ->
                    selectedAddress = address.street
                    selectedAddressId = address.id
                },
                onDismiss = {
                    showAddressSheet = false
                }
            )
        }
    }
}
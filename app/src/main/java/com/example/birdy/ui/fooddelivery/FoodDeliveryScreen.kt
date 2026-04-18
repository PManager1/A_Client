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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.birdy.data.FoodDeliveryData

/**
 * Food Delivery Screen — replicates iOS FoodDeliveryView.swift
 *
 * Sections (top to bottom):
 * 1. Header (address pin + notification bell + cart)
 * 2. Search bar (disabled, navigates to search on tap)
 * 3. Category horizontal scroll (20 emoji categories)
 * 4. Promo banner (blue gradient "Buy 2, save $2")
 * 5. Section 1: "Under $2 delivery fee" — restaurant cards
 * 6. Section 2: "Fastest near you" — restaurant cards
 * 7. Section 3: "Most loved" — restaurant cards
 * 8. Section 4: "Most Popular local" — restaurant cards
 */
@Composable
fun FoodDeliveryScreen(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onRestaurantClick: (restaurantName: String) -> Unit = {},
    onCategoryClick: (categoryName: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Address state (stub — will connect to AddressService later)
    var selectedAddress by remember { mutableStateOf<String?>(null) }
    var isLoadingAddress by remember { mutableStateOf(false) }

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
                    // TODO: Show address selection bottom sheet
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

            // MARK: - Promo Banner
            PromoBannerView(
                onOrderNow = { /* TODO: Navigate to promo */ },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MARK: - Section 1: Under $2 delivery fee
            RestaurantSection(
                title = "Under \$2 delivery fee",
                restaurants = FoodDeliveryData.section1Restaurants,
                onRestaurantClick = { onRestaurantClick(it.name) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MARK: - Section 2: Fastest near you
            RestaurantSection(
                title = "Fastest near you",
                restaurants = FoodDeliveryData.section2Restaurants,
                onRestaurantClick = { onRestaurantClick(it.name) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MARK: - Section 3: Most loved
            RestaurantSection(
                title = "Most loved",
                restaurants = FoodDeliveryData.section3Restaurants,
                onRestaurantClick = { onRestaurantClick(it.name) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MARK: - Section 4: Most Popular local
            RestaurantSection(
                title = "Most Popular local",
                restaurants = FoodDeliveryData.section4Restaurants,
                onRestaurantClick = { onRestaurantClick(it.name) }
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
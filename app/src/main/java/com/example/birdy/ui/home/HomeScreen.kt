package com.example.birdy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.birdy.data.HomeData
import com.example.birdy.ui.theme.NavyBlue

/**
 * Main Home Screen — replicates the iOS Home.swift layout.
 *
 * Sections (top to bottom):
 * 1. Header greeting ("U - DO" + user name)
 * 2. Animated search bar
 * 3. Location button ("Current: Washington, DC")
 * 4. Action buttons row (Post a Request, Food Select, FoodPlaces)
 * 5. Service Categories grid with shimmer loading
 * 6. Quick Re-order horizontal scroll
 * 7. Popular Near You horizontal scroll
 * 8. Trending searches list
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAllServices: () -> Unit = {},
    onNavigateToFoodDelivery: () -> Unit = {},
    onNavigateToFoodPlaces: () -> Unit = {},
    onCategoryClick: (categoryId: String, categoryName: String) -> Unit = { _, _ -> },
    onPopularClick: () -> Unit = {},
    onTrendingClick: (String) -> Unit = {}
) {
    // Read state from ViewModel
    val currentServiceIndex = viewModel.currentServiceIndex
    val userSelectedCategories = viewModel.userSelectedCategories
    val isLoadingCategories = viewModel.isLoadingCategories
    val userFirst = viewModel.userFirstName

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // Extra space for tab bar
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Header Greeting
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "U - DO",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${userFirst.ifEmpty { "Guest" }} 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Search Bar
            AnimatedSearchBar(
                currentService = HomeData.placeholderServices[currentServiceIndex],
                onClick = { onNavigateToSearch() },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // MARK: - Location Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    text = "Current: Washington, DC",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // MARK: - Action Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.AddCircle,
                    label = "Post a Request",
                    onClick = { /* TODO: Show request sheet */ }
                )
                ActionButton(
                    icon = Icons.Default.Menu,
                    label = "Food Select",
                    onClick = { onNavigateToFoodDelivery() }
                )
                ActionButton(
                    icon = Icons.Default.Star,
                    label = "FoodPlaces",
                    onClick = { onNavigateToFoodPlaces() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // MARK: - Service Categories Grid
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Top Services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                if (isLoadingCategories) {
                    // Shimmer loading state
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(4) {
                            CategoryIconShimmer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                } else if (userSelectedCategories.isEmpty()) {
                    Text(
                        text = "No services selected yet. Tap 'Discover More Services' to customize your home screen.",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                } else {
                    // Category grid (adaptive columns, ~3 per row)
                    userSelectedCategories.chunked(3).forEach { rowCategories ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowCategories.forEach { category ->
                                CategoryIconView(
                                    category = category,
                                    onClick = {
                                        onCategoryClick(category.id, category.name)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill remaining space if row isn't full
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // Discover More Services button
                DiscoverMoreButton(
                    onClick = { onNavigateToAllServices() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Quick Re-order
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Quick Re-order",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    HomeData.reorderItems.forEach { item ->
                        ReorderCardView(
                            item = item,
                            onBookAgain = { /* TODO: Re-book action */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Popular Near You
            Text(
                text = "Popular Near You",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                HomeData.popularItems.forEach { item ->
                    PopularItemCard(
                        item = item,
                        onClick = { onPopularClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // MARK: - Trending Searches
            Text(
                text = "Trending searches in Washington, DC",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HomeData.trendingSearches.forEach { searchItem ->
                TrendingSearchRow(
                    text = searchItem,
                    onClick = { onTrendingClick(searchItem) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
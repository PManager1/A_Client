package com.example.birdy.ui.explore

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.birdy.R
import com.example.birdy.data.ExploreCategory
import com.example.birdy.data.ExploreData

// Matches iOS ExploreView.swift
// - Top bar with "Explore" title + search icon
// - "Go to Food Places" blue button
// - 26 rows x 2 columns of CategoryCard tiles

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onNavigateToFoodPlaces: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onCategoryClick: (ExploreCategory) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // MARK: - Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Explore",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onNavigateToSearch() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // MARK: - Scrollable Content
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
        ) {
            // Category rows (2 cards per row)
            items(ExploreData.categories) { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExploreCategoryCard(
                        category = row.first,
                        onClick = { onCategoryClick(row.first) },
                        modifier = Modifier.weight(1f)
                    )
                    ExploreCategoryCard(
                        category = row.second,
                        onClick = { onCategoryClick(row.second) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// MARK: - Category Card
// Matches iOS CategoryCard: image + gradient overlay + title

@Composable
fun ExploreCategoryCard(
    category: ExploreCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "cardScale"
    )

    Box(
        modifier = modifier
            .height(180.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Background image — load from URL or local drawable (matching iOS bundled assets)
        val isUrl = category.imageName.startsWith("http")
        val localDrawableRes = localDrawableRes(category.imageName)

        if (isUrl) {
            AsyncImage(
                model = category.imageName,
                contentDescription = category.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (localDrawableRes != 0) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = localDrawableRes),
                contentDescription = category.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback: colored placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        // Gradient overlay (matches iOS: clear at 50% → black.opacity(0.5) at bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.5f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        // Title text at bottom-left
        Text(
            text = category.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
    }
}

// Maps iOS asset names to Android drawable resource IDs
// Matches the bundled images in res/drawable/ copied from iOS Assets.xcassets
private fun localDrawableRes(imageName: String): Int {
    return when (imageName) {
        "FastFood" -> R.drawable.fast_food
        "Pizza" -> R.drawable.pizza
        "Burger" -> R.drawable.burger
        "Cheesecake" -> R.drawable.cheesecake
        "Mexican" -> R.drawable.mexican
        "Indian-food" -> R.drawable.indian_food
        "Chicken" -> R.drawable.chicken
        "Chinese" -> R.drawable.chinese
        "Sandwich" -> R.drawable.sandwich
        "Coffee" -> R.drawable.coffee
        else -> 0 // no local drawable
    }
}

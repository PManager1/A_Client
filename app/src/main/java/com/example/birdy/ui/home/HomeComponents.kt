package com.example.birdy.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.birdy.data.HomePopularItem
import com.example.birdy.data.HomeReorderItem
import com.example.birdy.data.HomeServiceCategory
import com.example.birdy.ui.theme.NavyBlue
import com.example.birdy.ui.theme.OrangeSec5
import com.example.birdy.ui.theme.OrangeTitle
import com.example.birdy.ui.theme.ShimmerBase
import com.example.birdy.ui.theme.ShimmerHighlight

// ============================================================================
// MARK: - Search Bar with Animated Placeholder
// Matches iOS: searchBar computed property
// ============================================================================

@Composable
fun AnimatedSearchBar(
    currentService: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
            .background(Color(0xFFF3F3F3), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Red
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Search for",
            color = Color.Gray,
            fontSize = 17.sp
        )
        AnimatedContent(
            targetState = currentService,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith
                        fadeOut(animationSpec = tween(500)) using
                        SizeTransform(clip = false)
            },
            label = "animatedService"
        ) { service ->
            Text(
                text = " $service",
                color = OrangeTitle,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// MARK: - Category Icon View
// Matches iOS: CategoryIconView
// ============================================================================

@Composable
fun CategoryIconView(
    category: HomeServiceCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        // Map SF Symbol names to Material Icons
        val icon = mapCategoryIcon(category.icon)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(OrangeSec5, RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.name,
                tint = OrangeTitle,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleSmall,
            color = Color.DarkGray,
            maxLines = 2
        )
    }
}

/**
 * Maps iOS SF Symbol icon names to Material Icons.
 * When custom icons are added, this can be updated.
 */
private fun mapCategoryIcon(sfSymbolName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (sfSymbolName) {
        "bolt.fill" -> Icons.Default.Star // Placeholder; use bolt icon when available
        "wrench.fill" -> Icons.Default.Menu // Placeholder; use wrench icon when available
        else -> Icons.Default.Star
    }
}

// ============================================================================
// MARK: - Category Icon Shimmer (Loading State)
// Matches iOS: CategoryIconShimmerView
// ============================================================================

@Composable
fun CategoryIconShimmer(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(ShimmerBase.copy(alpha = shimmerAlpha), RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(14.dp)
                .background(ShimmerBase.copy(alpha = shimmerAlpha), RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(14.dp)
                .background(ShimmerBase.copy(alpha = shimmerAlpha), RoundedCornerShape(4.dp))
        )
    }
}

// ============================================================================
// MARK: - Reorder Card View
// Matches iOS: ReorderCardView
// ============================================================================

@Composable
fun ReorderCardView(
    item: HomeReorderItem,
    onBookAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(350.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Profile image
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Re-book ${item.name}",
                style = MaterialTheme.typography.titleLarge,
                color = NavyBlue
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        // Book Again button
        Box(
            modifier = Modifier
                .background(OrangeTitle, RoundedCornerShape(10.dp))
                .clickable { onBookAgain() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Book Again",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================================================
// MARK: - Popular Item Card
// Matches iOS: Popular Near You section with image cards
// ============================================================================

@Composable
fun PopularItemCard(
    item: HomePopularItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(200.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() }
    ) {
        // Background image
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Gradient overlay with text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.subtitle,
                color = Color.White,
                fontSize = 13.sp
            )
            // Star rating
            Row {
                repeat(item.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// MARK: - Trending Search Row
// Matches iOS: TrendingSearchRow
// ============================================================================

@Composable
fun TrendingSearchRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        HorizontalDivider()
    }
}

// ============================================================================
// MARK: - Discover More Button
// Matches iOS: "Discover More Services" button
// ============================================================================

@Composable
fun DiscoverMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(OrangeSec5.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Discover More Services",
            color = OrangeTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "→",
            color = OrangeTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ============================================================================
// MARK: - Action Button (Post Request, Food Select, FoodPlaces)
// Matches iOS: Action Buttons Row
// ============================================================================

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = OrangeTitle,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = OrangeTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
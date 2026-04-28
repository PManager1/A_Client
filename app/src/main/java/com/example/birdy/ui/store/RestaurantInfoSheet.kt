package com.example.birdy.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// MARK: - Restaurant Info Full-Screen Sheet (matches iOS RestaurantInfoSheet)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestaurantInfoSheet(
    data: StoreData,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. BANNER IMAGE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                model = data.brand_info.banner_image_url,
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            // Close button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = -10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 2. LOGO + NAME
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = data.brand_info.logo_url,
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = data.brand_info.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Text(
                        text = data.brand_info.cuisine,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            // 3. ADDRESS
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Address",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = Color(0xFFCC5500)
                    )
                    Text(
                        text = data.location_info.address,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            Divider(color = Color.LightGray)

            // 4. AVAILABLE HOURS
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Hours",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status indicator
                    Row(
                        modifier = Modifier
                            .background(Color.Green.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color.Green, CircleShape)
                        )
                        Text(
                            text = "Open Now",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Green
                        )
                    }

                    // Delivery time
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.location_info.delivery_time_est,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }

                // Show operating hours if available
                data.location_info.operating_hours?.let { hours ->
                    Spacer(modifier = Modifier.height(4.dp))
                    hours.forEach { (day, time) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = day,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = time,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Divider(color = Color.LightGray)

            // 5. RATING
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Rating",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star rating
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        for (star in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (star <= data.brand_info.rating.toInt()) Color(0xFFFFA000) else Color.LightGray
                            )
                        }
                    }
                    Text(
                        text = String.format("%.1f", data.brand_info.rating),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Text(
                        text = "(${data.brand_info.review_count} reviews)",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Divider(color = Color.LightGray)

            // 6. DELIVERY INFO
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Delivery Info",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Delivery fee
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFFFF0F0), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (data.location_info.delivery_fee == 0.0) "Free delivery"
                            else "$${String.format("%.2f", data.location_info.delivery_fee)} delivery fee",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCC1111)
                        )
                        Text(
                            text = "pricing & fees",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    // Delivery time
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = data.location_info.delivery_time_est,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "delivery time",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Divider(color = Color.LightGray)

            // 7. TAGS
            if (data.brand_info.tags.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Tags",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.brand_info.tags.forEach { tag ->
                            Text(
                                text = tag,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Divider(color = Color.LightGray)
            }

            // 8. MENU OVERVIEW
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Menu",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                val totalItems = data.menu.sumOf { it.items.size }
                Text(
                    text = "${data.menu.size} categories • $totalItems items",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                data.menu.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category.category_name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = "${category.items.size} items",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


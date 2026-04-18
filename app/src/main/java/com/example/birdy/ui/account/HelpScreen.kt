package com.example.birdy.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OrangeSecNavyBlue = Color(0xFF1B2A4A)
private val OrangeTitle = Color(0xFFF27836)
private val OrangeSec5 = Color(0xFFF5F0EB)

// Matches iOS HelpMenu.swift
@Composable
fun HelpScreen(
    onBack: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeSec5)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with back button and inbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Help",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = OrangeSecNavyBlue,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { /* TODO: navigate to inbox */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Inbox",
                    tint = OrangeTitle,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Inbox",
                    fontSize = 16.sp,
                    color = OrangeSecNavyBlue
                )
            }
        }

        // Search Bar
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    text = "Search help articles...",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = OrangeTitle
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F2F7),
                unfocusedContainerColor = Color(0xFFF2F2F7),
                disabledContainerColor = Color(0xFFF2F2F7),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // All Categories
        Text(
            text = "All Categories",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = OrangeSecNavyBlue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3-column grid
        val categories = listOf(
            HelpCategoryItem("My Trips", Icons.Default.DirectionsCar),
            HelpCategoryItem("Account", Icons.Default.Person),
            HelpCategoryItem("Earnings", Icons.Default.CreditCard),
            HelpCategoryItem("Deliveries", Icons.Default.Inventory),
            HelpCategoryItem("Safety", Icons.Default.Shield),
            HelpCategoryItem("Appointments", Icons.Default.CalendarMonth),
            HelpCategoryItem("Accessibility", Icons.Default.Accessible),
            HelpCategoryItem("Map Issues", Icons.Default.Map)
        )

        categories.chunked(3).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                row.forEach { category ->
                    CategoryTile(
                        category = category,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty slots if row has less than 3
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Bottom Contact Section
        Text(
            text = "Can't find what you're looking for?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OrangeSecNavyBlue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { /* TODO: contact us */ }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = OrangeTitle,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Contact Us",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

data class HelpCategoryItem(
    val name: String,
    val icon: ImageVector
)

@Composable
fun CategoryTile(
    category: HelpCategoryItem,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { /* TODO: navigate to category */ }
    ) {
        // Icon square
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.size(60.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = OrangeTitle,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = OrangeSecNavyBlue,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
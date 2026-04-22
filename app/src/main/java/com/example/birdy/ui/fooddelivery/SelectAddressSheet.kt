package com.example.birdy.ui.fooddelivery

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// MARK: - Mock Address Model (matches iOS Address)

data class Address(
    val id: String,
    val street: String,
    val cityStateZip: String,
    val isDefault: Boolean = false
)

// MARK: - Mock Addresses (will be replaced with AddressService API calls later)

private val mockAddresses = listOf(
    Address(id = "addr_1", street = "123 Main Street, Apt 4B", cityStateZip = "New York, NY 10001", isDefault = true),
    Address(id = "addr_2", street = "456 Broadway, Floor 12", cityStateZip = "New York, NY 10003", isDefault = false),
    Address(id = "addr_3", street = "789 Oak Avenue", cityStateZip = "Brooklyn, NY 11201", isDefault = false)
)

// MARK: - Select Address Bottom Sheet (matches iOS SelectAddress)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAddressSheet(
    currentAddressId: String?,
    onAddressSelected: (Address) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var selectedId by remember { mutableStateOf(currentAddressId) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.White)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Address",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                if (selectedId != null) {
                    // Green checkmark to confirm and close
                    IconButton(
                        onClick = {
                            // Find the selected address and notify parent
                            val selected = mockAddresses.find { it.id == selectedId }
                            if (selected != null) {
                                onAddressSelected(selected)
                            }
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirm",
                            tint = Color(0xFF4CAF50), // Green
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    // X button when nothing selected
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray.copy(alpha = 0.4f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))

            // Scrollable address list
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Current Location option
                AddressSelectionRow(
                    title = "Current Location",
                    subtitle = "Use your GPS location",
                    icon = Icons.Default.LocationOn,
                    isSelected = selectedId == "current_location",
                    onClick = {
                        selectedId = "current_location"
                        onAddressSelected(
                            Address(
                                id = "current_location",
                                street = "Current Location",
                                cityStateZip = "Using GPS"
                            )
                        )
                        onDismiss()
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = Color.Gray.copy(alpha = 0.2f)
                )

                // Saved addresses
                mockAddresses.forEach { address ->
                    AddressSelectionRow(
                        title = address.street,
                        subtitle = address.cityStateZip,
                        icon = if (address.isDefault) Icons.Default.Star else Icons.Default.LocationOn,
                        isSelected = selectedId == address.id,
                        onClick = {
                            selectedId = address.id
                            onAddressSelected(address)
                            onDismiss()
                        }
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = Color.Gray.copy(alpha = 0.2f)
                )

                // Add New Address button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Future: Navigate to address search/add */ }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Add New Address",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// MARK: - Address Selection Row (matches iOS AddressSelectionRow)

@Composable
private fun AddressSelectionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon circle
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color(0xFFF3F3F3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (icon == Icons.Default.Star) Color(0xFFFFC107) else Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Address text
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Checkmark or empty circle
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50), // Green
                modifier = Modifier.size(24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(Color.Transparent, CircleShape)
            )
        }
    }
}
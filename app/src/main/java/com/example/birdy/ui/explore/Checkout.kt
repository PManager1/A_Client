package com.example.birdy.ui.explore

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.birdy.data.CartManager

// MARK: - Mock Data Models (matches iOS Checkout.swift)

data class DeliveryAddress(
    val id: String,
    val title: String,
    val fullAddress: String,
    val instructions: String
)

data class PaymentMethod(
    val id: String,
    val type: String,
    val last4: String?,
    val brandIcon: String
)

// MARK: - Checkout Screen (matches iOS Checkout view)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit
) {
    // Mock data — matches iOS addresses/paymentMethods
    val addresses = remember {
        listOf(
            DeliveryAddress(
                id = "home",
                title = "Home",
                fullAddress = "123 Main Street, Apt 4B, New York, NY 10001",
                instructions = "Ring bell 3 times"
            ),
            DeliveryAddress(
                id = "work",
                title = "Work",
                fullAddress = "456 Broadway, Floor 12, New York, NY 10003",
                instructions = "Security will buzz you in"
            )
        )
    }

    val paymentMethods = remember {
        listOf(
            PaymentMethod(id = "gpay", type = "Google Pay", last4 = null, brandIcon = "gpay"),
            PaymentMethod(id = "visa", type = "Visa", last4 = "4242", brandIcon = "visa"),
            PaymentMethod(id = "mc", type = "Mastercard", last4 = "8888", brandIcon = "mc")
        )
    }

    var selectedAddress by remember { mutableStateOf(addresses.first()) }
    var selectedPayment by remember { mutableStateOf(paymentMethods.first()) }
    var tipAmount by remember { mutableStateOf(5.0) }
    var leaveAtDoor by remember { mutableStateOf(true) }
    var showOrderPlaced by remember { mutableStateOf(false) }

    val totalWithTip = CartManager.total + tipAmount

    // Order placed confirmation dialog
    if (showOrderPlaced) {
        OrderPlacedDialog(
            onDismiss = {
                CartManager.clear()
                showOrderPlaced = false
                onBack()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF2F2F7) // systemGroupedBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Delivery Address section
                DeliveryAddressSection(
                    addresses = addresses,
                    selectedAddress = selectedAddress,
                    onAddressSelected = { selectedAddress = it },
                    leaveAtDoor = leaveAtDoor,
                    onLeaveAtDoorChanged = { leaveAtDoor = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Payment Method section
                PaymentMethodSection(
                    paymentMethods = paymentMethods,
                    selectedPayment = selectedPayment,
                    onPaymentSelected = { selectedPayment = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Order Items section
                OrderItemsSection()

                Spacer(modifier = Modifier.height(20.dp))

                // Tip section
                TipSection(
                    tipAmount = tipAmount,
                    onTipSelected = { tipAmount = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Summary section
                SummarySection(
                    tipAmount = tipAmount,
                    totalWithTip = totalWithTip
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Floating Place Order button at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Place Order • $${String.format("%.2f", totalWithTip)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFCC5500), // burntOrange
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            showOrderPlaced = true
                        }
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// MARK: - Delivery Address Section (matches iOS deliverySection)

@Composable
private fun DeliveryAddressSection(
    addresses: List<DeliveryAddress>,
    selectedAddress: DeliveryAddress,
    onAddressSelected: (DeliveryAddress) -> Unit,
    leaveAtDoor: Boolean,
    onLeaveAtDoorChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Delivery Address",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        addresses.forEach { address ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .clickable { onAddressSelected(address) }
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Address icon
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFCC5500),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Address info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = address.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = address.fullAddress,
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                    if (address.instructions.isNotEmpty()) {
                        Text(
                            text = "Note: ${address.instructions}",
                            fontSize = 12.sp,
                            color = Color(0xFF2196F3) // Blue
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Checkmark
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (selectedAddress.id == address.id) Color(0xFFCC5500) else Color.Gray.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add new address button
        Text(
            text = "Add new address",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3),
            modifier = Modifier
                .clickable { /* Future: Add address flow */ }
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Leave at door toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Leave at door",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Switch(
                checked = leaveAtDoor,
                onCheckedChange = onLeaveAtDoorChanged,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFFCC5500),
                    checkedThumbColor = Color.White
                )
            )
        }
    }
}

// MARK: - Payment Method Section (matches iOS paymentSection)

@Composable
private fun PaymentMethodSection(
    paymentMethods: List<PaymentMethod>,
    selectedPayment: PaymentMethod,
    onPaymentSelected: (PaymentMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Payment Method",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        paymentMethods.forEach { method ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .clickable { onPaymentSelected(method) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand icon
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Payment info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = method.type,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (method.last4 != null) {
                        Text(
                            text = "•••• ${method.last4}",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Checkmark
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (selectedPayment.id == method.id) Color(0xFFCC5500) else Color.Gray.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// MARK: - Order Items Section (matches iOS orderItemsSection)

@Composable
private fun OrderItemsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Your Order",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        CartManager.items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Food image
                AsyncImage(
                    model = item.imageURL,
                    contentDescription = item.dishName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Item info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.dishName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "${item.quantity} × $${String.format("%.2f", item.price)}",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// MARK: - Tip Section (matches iOS tipSection)

@Composable
private fun TipSection(
    tipAmount: Double,
    onTipSelected: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tip Your Driver",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(0.0, 2.0, 5.0, 10.0).forEach { tip ->
                val isSelected = tipAmount == tip
                val label = if (tip == 0.0) "No tip" else "$${tip.toInt()}"

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) Color(0xFFCC5500) else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onTipSelected(tip) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

// MARK: - Summary Section (matches iOS summarySection)

@Composable
private fun SummarySection(
    tipAmount: Double,
    totalWithTip: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Order Summary",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        CheckoutPriceRow(title = "Subtotal", amount = CartManager.subtotal)
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutPriceRow(title = "Delivery Fee", amount = CartManager.deliveryFee)
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutPriceRow(title = "Service Fee", amount = CartManager.serviceFee)
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutPriceRow(title = "Tax", amount = CartManager.tax)
        Spacer(modifier = Modifier.height(8.dp))
        CheckoutPriceRow(title = "Tip", amount = tipAmount)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Gray.copy(alpha = 0.3f)
        )

        CheckoutPriceRow(title = "Total", amount = totalWithTip, isBold = true)
    }
}

// MARK: - Price Row (matches iOS PriceRow_CO)

@Composable
private fun CheckoutPriceRow(
    title: String,
    amount: Double,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = if (isBold) 20.sp else 17.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) Color.Black else Color.Gray
        )
        Text(
            text = "$${String.format("%.2f", amount)}",
            fontSize = if (isBold) 20.sp else 17.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) Color.Black else Color.Gray
        )
    }
}

// MARK: - Order Placed Dialog (matches iOS OrderPlacedView)

@Composable
private fun OrderPlacedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        confirmButton = {},
        title = null,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFCC5500),
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "Order Placed!",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Your delicious food is on the way",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Estimated arrival: 32–42 min",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCC5500),
                    modifier = Modifier
                        .background(
                            Color(0xFFCC5500).copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Track Order button
                Text(
                    text = "Track Order",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .width(300.dp)
                        .background(Color(0xFFCC5500), RoundedCornerShape(16.dp))
                        .clickable { onDismiss() }
                        .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}
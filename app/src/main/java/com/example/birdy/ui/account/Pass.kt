package com.example.birdy.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BurntOrange = Color(0xFFCC5500)
private val BackgroundGrey = Color(0xFFF2F2F7)

// Matches iOS Pass.swift (PassView)
@Composable
fun PassScreen(
    onBack: () -> Unit = {}
) {
    var selectedPlan by remember { mutableStateOf("Monthly") }
    var agreedToTerms by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(16.dp)
                            .background(BackgroundGrey, CircleShape)
                            .padding(10.dp)
                    )
                }
            }

            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Header
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "U-DO Pass",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = BurntOrange
                    )
                    Text(
                        text = "The best of your neighborhood,\ndelivered for less.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }

                // Benefits Grid (2x2)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BenefitCard(
                        icon = "📦",
                        title = "$0 Delivery",
                        subtitle = "On eligible orders",
                        modifier = Modifier.weight(1f)
                    )
                    BenefitCard(
                        icon = "💰",
                        title = "Lower Fees",
                        subtitle = "5% back on pickup",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BenefitCard(
                        icon = "⭐",
                        title = "Priority",
                        subtitle = "Top-rated workers",
                        modifier = Modifier.weight(1f)
                    )
                    BenefitCard(
                        icon = "🔄",
                        title = "Flexible",
                        subtitle = "Cancel anytime",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Plan Selection
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Choose your plan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Monthly
                    PlanCard(
                        title = "Monthly Plan",
                        price = "$9.99",
                        period = "/mo",
                        isSelected = selectedPlan == "Monthly",
                        onClick = { selectedPlan = "Monthly" }
                    )

                    // Annual
                    PlanCard(
                        title = "Annual Plan",
                        price = "$8.00",
                        period = "/mo",
                        subtitle = "($96 /year, billed yearly)",
                        isSelected = selectedPlan == "Annual",
                        badge = "Best Value",
                        onClick = { selectedPlan = "Annual" }
                    )
                }

                // Legal
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    // Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { agreedToTerms = !agreedToTerms }
                    ) {
                        Icon(
                            imageVector = if (agreedToTerms) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Terms",
                            tint = if (agreedToTerms) Color(0xFFFF9500) else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I agree to a paid subscription after my free trial.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "By tapping Start 30-day free trial, you agree to the U-DO Terms & Conditions. Your subscription will auto-renew until you cancel.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Fixed bottom button
        Button(
            onClick = { /* TODO: start trial */ },
            enabled = agreedToTerms,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (agreedToTerms) BurntOrange else Color.Gray.copy(alpha = 0.4f),
                disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
            )
        ) {
            Text(
                text = "Start 30-day free trial",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun BenefitCard(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BackgroundGrey,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = icon, fontSize = 20.sp)
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    period: String,
    subtitle: String? = null,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BackgroundGrey,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFFFF9500), RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (badge != null) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                    Text(
                        text = period,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
            }
            Icon(
                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Select",
                tint = if (isSelected) Color(0xFFFF9500) else Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
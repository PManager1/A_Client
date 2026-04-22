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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val OrangeSecNavyBlue = Color(0xFF1B2A4A)
private val BurntOrange = Color(0xFFCC5500)
private val SoftGrey = Color(0xFFF2F2F7)

// Matches iOS Wallet.swift (WalletViewV2)
@Composable
fun WalletScreen(
    onBack: () -> Unit = {}
) {
    var currentBalance by remember { mutableStateOf(25.50) }
    var amountToAdd by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Wallet",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // MARK: - Balance Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp)
        ) {
            Text(
                text = "Current Balance",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = String.format(Locale.US, "$%.2f", currentBalance),
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // MARK: - Add Funds Input
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Text(
                    text = "Add Funds",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Dollar input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(SoftGrey)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "$",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    TextField(
                        value = amountToAdd,
                        onValueChange = { amountToAdd = it },
                        placeholder = {
                            Text(
                                text = "0.00",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = true
                    )
                }

                // Quick Select Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("10", "25", "50", "100").forEach { amount ->
                        val isSelected = amountToAdd == amount
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) BurntOrange else SoftGrey,
                            modifier = Modifier.clickable { amountToAdd = amount }
                        ) {
                            Text(
                                text = "+$amount",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color.Black,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // MARK: - Recent Activity
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Text(
                    text = "Recent Activity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                repeat(3) {
                    ActivityRow()
                }
            }
        }

        // MARK: - Action Button (fixed at bottom)
        Button(
            onClick = {
                val amount = amountToAdd.toDoubleOrNull()
                if (amount != null) {
                    currentBalance += amount
                    amountToAdd = ""
                }
            },
            enabled = amountToAdd.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BurntOrange,
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "Confirm & Add Funds",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ActivityRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoftGrey.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        // Circle icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF9500).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "Funds Added",
                tint = Color(0xFFFF9500),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Funds Added",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "April 8, 2026",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        Text(
            text = "+$25.00",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}
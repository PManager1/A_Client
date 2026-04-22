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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OrangeTitle = Color(0xFFF27836)

// Matches iOS ManageAccountView.swift
@Composable
fun ManageAccountScreen(
    onBack: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Title
            Text(
                text = "Manage Account",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp, top = 40.dp, end = 16.dp)
            )

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Account Actions Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF2F2F7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Sign In
                    AccountActionRow(
                        icon = Icons.Default.Person,
                        title = "Sign In",
                        iconColor = OrangeTitle,
                        textColor = Color.Black,
                        onClick = onSignIn
                    )

                    HorizontalDivider(
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Sign Out
                    AccountActionRow(
                        icon = Icons.Default.ExitToApp,
                        title = "Sign Out",
                        iconColor = OrangeTitle,
                        textColor = Color.Black,
                        onClick = onSignOut
                    )

                    HorizontalDivider(
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Delete Account
                    AccountActionRow(
                        icon = Icons.Default.Delete,
                        title = "Delete Account",
                        iconColor = Color.Red,
                        textColor = Color.Red,
                        onClick = onDeleteAccount
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Close button (top-trailing, matches iOS overlay)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun AccountActionRow(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
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
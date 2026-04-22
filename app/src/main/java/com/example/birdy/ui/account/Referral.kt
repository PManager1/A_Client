package com.example.birdy.ui.account

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val OrangeSecNavyBlue = Color(0xFF1B2A4A)
private val BurntOrange = Color(0xFFCC5500)
private val OrangeSec2 = Color(0xFF8E8E93)
private val OrangeSec3 = Color(0xFFFF9500)
private val OrangeSec5 = Color(0xFFF5F0EB)
private val OrangeSec6 = Color(0xFFE5E5EA)
private val OrangeSec7 = Color(0xFF1C1C1E)

// Matches iOS ReferralView.swift
@Composable
fun ReferralScreen(
    onBack: () -> Unit = {}
) {
    var invitees by remember { mutableStateOf(0) }
    var totalEarned by remember { mutableStateOf(0.0) }
    var showingInviteOptions by remember { mutableStateOf(false) }
    var showingInvites by remember { mutableStateOf(false) }

    val rewardDetail = "You earn \$50 for every 20 services booked by your referrals."
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeSec5)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = OrangeSecNavyBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            // Title and Illustration
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Refer friends",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSecNavyBlue,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Refer",
                    tint = OrangeSec3,
                    modifier = Modifier.size(50.dp)
                )
            }

            HorizontalDivider(
                color = OrangeSec6,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Referral Rules & Reward
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Invite friends who are new to Birdy",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    color = OrangeSec7
                )

                Text(
                    text = "Refer Friends, Earn Big—up to \$500!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSec7
                )

                // Reward Detail Box
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Car",
                            tint = BurntOrange,
                            modifier = Modifier.size(30.dp, 20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Get up to \$400 for referrals",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeSec7
                            )
                            Text(
                                text = rewardDetail,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = OrangeSec2
                            )
                        }
                    }
                }

                // Show all rewards button
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White
                ) {
                    Text(
                        text = "Show all rewards",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeSecNavyBlue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: navigate to all rewards */ }
                            .padding(16.dp)
                    )
                }
            }

            // Separator
            HorizontalDivider(
                color = OrangeSec6,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Status Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Status",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSec7
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Column {
                        Text(
                            text = "Invitees",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = OrangeSec2
                        )
                        Text(
                            text = "$invitees",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeSecNavyBlue
                        )
                    }

                    Column {
                        Text(
                            text = "You earned",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = OrangeSec2
                        )
                        Text(
                            text = "$${"%.0f".format(totalEarned)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeSecNavyBlue
                        )
                    }
                }

                // Show invites button
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White
                ) {
                    Text(
                        text = "Show invites",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeSecNavyBlue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showingInvites = true }
                            .padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Invite Button
            Button(
                onClick = { showingInviteOptions = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeSecNavyBlue
                )
            ) {
                Text(
                    text = "Invite",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Sheet - Invite Options
        AnimatedVisibility(
            visible = showingInviteOptions,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(bottom = 40.dp)
            ) {
                // Title
                Text(
                    text = "Send invite",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSec7,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                        .padding(horizontal = 16.dp)
                )

                HorizontalDivider()

                // Invite from contacts
                Text(
                    text = "Invite from contacts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showingInviteOptions = false
                            val message = "Hey, check out Birdy! Use my referral link to get started: [Your Referral Link Here]"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, message)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Invite via"))
                        }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                // Share your link
                Text(
                    text = "Share your link",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showingInviteOptions = false
                            val link = "[Your Referral Link Here]"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, link)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                        }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                )

                HorizontalDivider()

                // Cancel
                Text(
                    text = "Cancel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showingInviteOptions = false }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                )
            }
        }

        // Invites Sheet (simple overlay)
        AnimatedVisibility(
            visible = showingInvites,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Your Invites",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeSecNavyBlue
                        )
                        IconButton(onClick = { showingInvites = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "No invites yet. Start inviting friends!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = OrangeSec2
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
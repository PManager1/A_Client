package com.example.birdy.ui.account

import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun ReferralScreen(
    onBack: () -> Unit = {},
    onNavigateToReferralCode: () -> Unit = {}
) {
    var invitees by remember { mutableStateOf(0) }
    var totalEarned by remember { mutableStateOf(0.0) }
    var showingInviteOptions by remember { mutableStateOf(false) }
    var showingInvites by remember { mutableStateOf(false) }
    var showSMSError by remember { mutableStateOf(false) }
    var selectedPhoneNumbers by remember { mutableStateOf(listOf<String>()) }

    val rewardDetail = "You also earn money as your referrals make money."
    val inviteMessage = "Hey! Try U-DO for food. No inflated menu prices or shady fees like Uber/DoorDash. It's good for us and pays drivers more. Get it on https://udonow.com "

    val context = LocalContext.current

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let {
            val phoneNumbers = mutableListOf<String>()
            context.contentResolver.query(
                it, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER), null, null, null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val number = cursor.getString(0)
                    val cleaned = number.replace(Regex("[^+0-9]"), "")
                    phoneNumbers.add(cleaned)
                }
            }
            if (phoneNumbers.isNotEmpty()) {
                selectedPhoneNumbers = phoneNumbers
                val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("sms:${phoneNumbers.joinToString(",")}")).apply {
                    putExtra("sms_body", inviteMessage)
                }
                try {
                    context.startActivity(smsIntent)
                } catch (e: Exception) {
                    showSMSError = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeSec5)
                .verticalScroll(rememberScrollState())
        ) {
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

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Invite friends who are new to U-DO",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    color = OrangeSec7
                )

                Text(
                    text = "Refer Friends, Earn Big - up to \$500! per referral",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSec7
                )

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
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Dollar",
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

                Spacer(modifier = Modifier.height(16.dp))
            }

            HorizontalDivider(
                color = OrangeSec6,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

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
                        val earnedText = "$" + "%.0f".format(totalEarned)
                        Text(
                            text = earnedText,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeSecNavyBlue
                        )
                    }
                }

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
                Text(
                    text = "Send invite",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeSec7,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 16.dp)
                )

                HorizontalDivider()

                Text(
                    text = "Invite from contacts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showingInviteOptions = false
                            try {
                                contactPickerLauncher.launch(null)
                            } catch (e: Exception) {
                                showSMSError = true
                            }
                        }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                Text(
                    text = "Share your link",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showingInviteOptions = false
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, inviteMessage)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                        }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                )

                HorizontalDivider()

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

        AnimatedVisibility(
            visible = showingInvites,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(onClick = { showingInvites = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back",
                            tint = OrangeSecNavyBlue
                        )
                    }
                    Text(
                        text = "Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeSec7
                    )
                    IconButton(onClick = { /* Help */ }) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Help",
                            tint = OrangeSec7
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Status",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeSec7
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = OrangeSec2
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Invitees", fontSize = 15.sp, color = OrangeSec2)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$invitees", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = OrangeSec7)
                        }
                        Spacer(modifier = Modifier.width(20.dp))

                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .width(1.dp)
                                .background(OrangeSec6)
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = OrangeSec2
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("You earned", fontSize = 15.sp, color = OrangeSec2)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val earnedText = "$" + "%.0f".format(totalEarned)
                            Text(earnedText, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = OrangeSec7)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { /* active tab */ },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Active Invites", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OrangeSec7)
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(OrangeSec7)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { /* past tab */ },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Past Invites", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OrangeSec2)
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Color.Transparent)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF007AFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "No invites",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "No active invitations",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeSec7
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Invite friends to U-DO and earn rewards. When they join, you'll see them listed here.",
                            fontSize = 15.sp,
                            color = OrangeSec2,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { /* Learn more */ },
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            modifier = Modifier.padding(horizontal = 40.dp)
                        ) {
                            Text(
                                "Learn more",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp))
                            }
                        }
                    }
                }
            }
        }

    if (showSMSError) {
        AlertDialog(
            onDismissRequest = { showSMSError = false },
            title = { Text("Cannot Send Messages") },
            text = { Text("This device cannot send text messages. Please use 'Share your link' instead.") },
            confirmButton = { TextButton(onClick = { showSMSError = false }) { Text("OK") } }
        )
    }
}

package com.norton.scamdetector.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.norton.scamdetector.ui.theme.NortonColors

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NortonColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Avatar + Welcome row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(NortonColors.ShieldGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Welcome",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NortonColors.TextPrimary
                )
                Text(
                    text = "user@example.com",
                    color = NortonColors.TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SUBSCRIPTION section
        ProfileSectionHeader(label = "SUBSCRIPTION")
        Spacer(modifier = Modifier.height(8.dp))

        // Subscription card — green left border + light green background
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left accent border
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(140.dp)
                        .background(NortonColors.SafeGreen)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF1F8F1))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(NortonColors.SafeGreen)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "Active",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = NortonColors.ShieldGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Norton Mobile Security",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NortonColors.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your subscription is protecting your device",
                            fontSize = 12.sp,
                            color = NortonColors.TextSecondary,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NortonColors.TextPrimary
                            )
                        ) {
                            Text(text = "Upgrade options", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ACCOUNT section
        ProfileSectionHeader(label = "ACCOUNT")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenuCard {
            ProfileMenuItem(
                icon = Icons.Filled.ManageAccounts,
                label = "Manage account",
                trailingIcon = Icons.Filled.ChevronRight,
                onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = NortonColors.DividerColor,
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = Icons.Filled.PhoneAndroid,
                label = "Manage devices",
                sublabel = "1/1 devices used",
                trailingIcon = Icons.Filled.Add,
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // MORE section
        ProfileSectionHeader(label = "MORE")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileMenuCard {
            ProfileMenuItem(
                icon = Icons.Filled.Settings,
                label = "Privacy Settings",
                trailingIcon = Icons.Filled.ChevronRight,
                onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = NortonColors.DividerColor,
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.Help,
                label = "Help and support",
                trailingIcon = Icons.Filled.ChevronRight,
                onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = NortonColors.DividerColor,
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = Icons.Filled.Info,
                label = "About",
                trailingIcon = Icons.Filled.ChevronRight,
                onClick = {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = NortonColors.DividerColor,
                thickness = 0.5.dp
            )
            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                label = "Sign out",
                labelColor = NortonColors.DangerousRed,
                trailingIcon = Icons.Filled.ChevronRight,
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileSectionHeader(label: String) {
    Text(
        text = label,
        modifier = Modifier.padding(horizontal = 20.dp),
        color = NortonColors.TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp
    )
}

@Composable
private fun ProfileMenuCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Column { content() }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    sublabel: String? = null,
    labelColor: Color = NortonColors.TextPrimary,
    trailingIcon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            .background(NortonColors.CardBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NortonColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
            if (sublabel != null) {
                Text(
                    text = sublabel,
                    color = NortonColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = NortonColors.TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}
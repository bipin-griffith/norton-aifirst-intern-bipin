package com.norton.scamdetector.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.norton.scamdetector.ui.theme.NortonColors
import com.norton.scamdetector.ui.viewmodel.ScamDetectorViewModel

@Composable
fun HomeScreen(viewModel: ScamDetectorViewModel) {
    val totalScans by viewModel.totalScans.collectAsStateWithLifecycle()
    val dangerousCount by viewModel.dangerousCount.collectAsStateWithLifecycle()
    val threatsAvoided = (totalScans - dangerousCount).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NortonColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Norton logo
        NortonLogo()

        // Alert banner
        AlertBannerCard(dangerousCount = dangerousCount)

        // Wi-Fi Security card
        WifiSecurityCard()

        // Protection Report card
        ProtectionReportCard(
            totalScans = totalScans,
            threatsAvoided = threatsAvoided
        )

        // Scam Protection card
        ScamProtectionCard()

        // Dark Web + VPN row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DarkWebCard(modifier = Modifier.weight(1f))
            VpnCard(modifier = Modifier.weight(1f))
        }

        // Device count row
        DeviceCountRow()

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun NortonLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            drawCircle(color = Color(0xFFFFD700))
            val stroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            val cx = size.width
            val cy = size.height
            // Checkmark: short left leg then long right leg
            drawLine(
                color = Color.White,
                start = Offset(cx * 0.25f, cy * 0.50f),
                end = Offset(cx * 0.43f, cy * 0.68f),
                strokeWidth = stroke.width,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(cx * 0.43f, cy * 0.68f),
                end = Offset(cx * 0.76f, cy * 0.33f),
                strokeWidth = stroke.width,
                cap = StrokeCap.Round
            )
        }
        Text(
            text = "norton",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = NortonColors.TextPrimary,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
private fun AlertBannerCard(dangerousCount: Int) {
    val isDanger = dangerousCount > 0
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDanger) NortonColors.DangerousRedLight else NortonColors.SafeGreenLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (isDanger) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (isDanger) NortonColors.DangerousRed else NortonColors.SafeGreen,
                modifier = Modifier.size(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isDanger) "Core protection needed" else "You are protected",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isDanger) NortonColors.DangerousRed else NortonColors.SafeGreen
                )
                Text(
                    text = if (isDanger) "$dangerousCount threat(s) detected in recent scans"
                           else "All systems running normally",
                    fontSize = 12.sp,
                    color = NortonColors.TextSecondary
                )
            }
            if (isDanger) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NortonColors.PrimaryYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Fix now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun WifiSecurityCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NortonColors.SafeGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    contentDescription = null,
                    tint = NortonColors.SafeGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Secure network",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = NortonColors.TextPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NortonColors.ShieldGreen)
                    )
                    Text(
                        text = "Home Network",
                        fontSize = 12.sp,
                        color = NortonColors.TextSecondary
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = NortonColors.ShieldGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProtectionReportCard(totalScans: Int, threatsAvoided: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Protection Report",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NortonColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ring progress indicator
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 8.dp.toPx()
                        val inset = strokeW / 2f
                        val arcSize = Size(size.width - strokeW, size.height - strokeW)

                        // Background ring
                        drawArc(
                            color = Color(0xFFE0E0E0),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        // Filled portion (proportion of safe scans)
                        val sweep = if (totalScans > 0) {
                            (threatsAvoided.toFloat() / totalScans) * 360f
                        } else 0f

                        if (sweep > 0f) {
                            drawArc(
                                color = NortonColors.ProtectionRing,
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = arcSize,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                        }
                    }
                    // Center count
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = threatsAvoided.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = NortonColors.TextPrimary
                        )
                        Text(
                            text = "avoided",
                            fontSize = 9.sp,
                            color = NortonColors.TextSecondary
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Scans this session",
                        fontSize = 12.sp,
                        color = NortonColors.TextSecondary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(NortonColors.ProtectionRing)
                        )
                        Text(
                            text = "Device",
                            fontSize = 13.sp,
                            color = NortonColors.TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "$totalScans total scans",
                        fontSize = 12.sp,
                        color = NortonColors.TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ScamProtectionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NortonColors.SafeGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = NortonColors.ShieldGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Scam Protection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NortonColors.TextPrimary
                )
                Text(
                    text = "Included in your plan",
                    fontSize = 12.sp,
                    color = NortonColors.TextSecondary
                )
            }
            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1565C0))
            ) {
                Text(text = "Set up ›", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun DarkWebCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = NortonColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Dark Web Monitoring",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NortonColors.TextPrimary,
                lineHeight = 16.sp
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NortonColors.PrimaryYellow,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Set Up", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun VpnCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = null,
                tint = NortonColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "VPN",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = NortonColors.TextPrimary,
                lineHeight = 16.sp
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NortonColors.PrimaryYellow,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Enable", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DeviceCountRow() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PhoneAndroid,
                contentDescription = null,
                tint = NortonColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "1/1 devices used",
                fontSize = 14.sp,
                color = NortonColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1565C0))
            ) {
                Text(text = "Manage", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
package com.norton.scamdetector.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Telephony
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.norton.scamdetector.data.model.ExampleMessage
import com.norton.scamdetector.ui.components.AnalysisResultCard
import com.norton.scamdetector.ui.components.ExampleMessageChip
import com.norton.scamdetector.ui.components.ScanHistoryList
import com.norton.scamdetector.ui.components.StatsDashboard
import com.norton.scamdetector.ui.theme.NortonColors
import com.norton.scamdetector.ui.viewmodel.ScamDetectorUiState
import com.norton.scamdetector.ui.viewmodel.ScamDetectorViewModel

private data class SmsItem(val sender: String, val body: String)

private fun readInboxMessages(context: Context, limit: Int = 5): List<SmsItem> {
    val result = mutableListOf<SmsItem>()
    val cursor = context.contentResolver.query(
        Telephony.Sms.Inbox.CONTENT_URI,
        arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY),
        null, null,
        "${Telephony.Sms.DATE} DESC"
    ) ?: return result
    cursor.use {
        val addrIdx = it.getColumnIndex(Telephony.Sms.ADDRESS)
        val bodyIdx = it.getColumnIndex(Telephony.Sms.BODY)
        while (it.moveToNext() && result.size < limit) {
            result.add(
                SmsItem(
                    sender = if (addrIdx >= 0) it.getString(addrIdx) ?: "Unknown" else "Unknown",
                    body = if (bodyIdx >= 0) it.getString(bodyIdx) ?: "" else ""
                )
            )
        }
    }
    return result
}

@Composable
fun ScamDetectorScreen(viewModel: ScamDetectorViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentInput by viewModel.currentInput.collectAsStateWithLifecycle()
    val totalScans by viewModel.totalScans.collectAsStateWithLifecycle()
    val dangerousCount by viewModel.dangerousCount.collectAsStateWithLifecycle()
    val recentHistory by viewModel.recentHistory.collectAsStateWithLifecycle()
    val isLoading = uiState is ScamDetectorUiState.Loading

    val safeCount = (totalScans - dangerousCount).coerceAtLeast(0)

    var smsPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var inboxMessages by remember { mutableStateOf<List<SmsItem>>(emptyList()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        smsPermissionGranted = granted
    }

    LaunchedEffect(smsPermissionGranted) {
        if (smsPermissionGranted) {
            inboxMessages = readInboxMessages(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NortonColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = "Norton Scam Detector",
                    tint = NortonColors.PrimaryYellow,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Scam Detector",
                    color = NortonColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Text(
                text = "Paste a suspicious SMS, email, or URL below to check it for scam signals.",
                color = NortonColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            // SMS Permission gate
            if (!smsPermissionGranted) {
                SmsPermissionCard(onGrantClick = {
                    permissionLauncher.launch(Manifest.permission.READ_SMS)
                })
            } else {
                InboxScannerCard(
                    messages = inboxMessages,
                    onScanClick = { text ->
                        viewModel.updateInput(text)
                        viewModel.analyzeMessage()
                    }
                )
            }

            // Stats dashboard
            StatsDashboard(
                totalScans = totalScans,
                dangerousCount = dangerousCount,
                safeCount = safeCount
            )

            // Message input
            OutlinedTextField(
                value = currentInput,
                onValueChange = { viewModel.updateInput(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Paste a message, URL, or email snippet...",
                        color = NortonColors.TextSecondary,
                        fontSize = 14.sp
                    )
                },
                minLines = 4,
                maxLines = 8,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NortonColors.TextPrimary,
                    unfocusedTextColor = NortonColors.TextPrimary,
                    focusedBorderColor = NortonColors.PrimaryYellow,
                    unfocusedBorderColor = NortonColors.TextSecondary.copy(alpha = 0.4f),
                    cursorColor = NortonColors.PrimaryYellow,
                    focusedContainerColor = NortonColors.Surface,
                    unfocusedContainerColor = NortonColors.Surface,
                    disabledContainerColor = NortonColors.Surface,
                    disabledTextColor = NortonColors.TextSecondary,
                    disabledBorderColor = NortonColors.TextSecondary.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(10.dp)
            )

            // Example chips
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Try an example:",
                    color = NortonColors.TextSecondary,
                    fontSize = 12.sp
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExampleMessage.getExamples().forEach { example ->
                        ExampleMessageChip(
                            example = example,
                            onClick = { text -> viewModel.updateInput(text) }
                        )
                    }
                }
            }

            // Analyze button
            Button(
                onClick = { viewModel.analyzeMessage() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading && currentInput.trim().isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NortonColors.PrimaryYellow,
                    contentColor = Color.Black,
                    disabledContainerColor = NortonColors.PrimaryYellow.copy(alpha = 0.4f),
                    disabledContentColor = Color.Black.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Analyze Message",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Analysis result
            AnimatedVisibility(
                visible = uiState is ScamDetectorUiState.Success,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
            ) {
                val result = (uiState as? ScamDetectorUiState.Success)?.result
                if (result != null) {
                    AnalysisResultCard(result = result)
                }
            }

            // Error state
            AnimatedVisibility(visible = uiState is ScamDetectorUiState.Error) {
                val errorMessage = (uiState as? ScamDetectorUiState.Error)?.message ?: ""
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = NortonColors.DangerousRed.copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Analysis Failed",
                            color = NortonColors.DangerousRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = errorMessage,
                            color = NortonColors.TextSecondary,
                            fontSize = 13.sp
                        )
                        TextButton(
                            onClick = { viewModel.clearResult() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = NortonColors.PrimaryYellow
                            )
                        ) {
                            Text(text = "Try Again", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Scan history section
            AnimatedVisibility(
                visible = recentHistory.isNotEmpty() || totalScans > 0,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Recent Scans",
                            color = NortonColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = { viewModel.clearHistory() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = NortonColors.TextSecondary
                            )
                        ) {
                            Text(text = "Clear History", fontSize = 12.sp)
                        }
                    }
                    ScanHistoryList(items = recentHistory)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SmsPermissionCard(onGrantClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NortonColors.PrimaryYellow, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = NortonColors.PrimaryYellow,
                modifier = Modifier
                    .size(28.dp)
                    .padding(top = 2.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Scan your messages",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = NortonColors.TextPrimary
                )
                Text(
                    text = "Allow access to detect scams in your SMS inbox",
                    fontSize = 13.sp,
                    color = NortonColors.TextSecondary,
                    lineHeight = 18.sp
                )
                Button(
                    onClick = onGrantClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NortonColors.PrimaryYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Grant Permission", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun InboxScannerCard(
    messages: List<SmsItem>,
    onScanClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = NortonColors.CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Inbox,
                    contentDescription = null,
                    tint = NortonColors.ShieldGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Inbox Scanner",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NortonColors.TextPrimary
                )
            }

            if (messages.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No messages found in inbox.",
                    fontSize = 13.sp,
                    color = NortonColors.TextSecondary
                )
            } else {
                messages.forEachIndexed { index, sms ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = NortonColors.DividerColor,
                            thickness = 0.5.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sms.sender,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = NortonColors.TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = sms.body.take(60).let {
                                    if (sms.body.length > 60) "$it…" else it
                                },
                                fontSize = 12.sp,
                                color = NortonColors.TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 16.sp
                            )
                        }
                        Button(
                            onClick = { onScanClick(sms.body) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NortonColors.PrimaryYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(32.dp),
                        ) {
                            Text(text = "Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
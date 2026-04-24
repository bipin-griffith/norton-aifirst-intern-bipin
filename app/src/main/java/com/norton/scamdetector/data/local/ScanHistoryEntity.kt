package com.norton.scamdetector.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val messageText: String,
    val messagePreview: String,
    val riskLevel: String,
    val confidenceScore: Int,
    val explanation: String,
    val redFlags: String,
    val scannedAt: Long
)

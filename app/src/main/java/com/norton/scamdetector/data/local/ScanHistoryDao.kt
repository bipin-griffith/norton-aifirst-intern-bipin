package com.norton.scamdetector.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Insert
    suspend fun insertScan(entity: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun getAllScans(): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC LIMIT :limit")
    fun getRecentScans(limit: Int): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history WHERE riskLevel = :riskLevel ORDER BY scannedAt DESC")
    fun getScansByRiskLevel(riskLevel: String): Flow<List<ScanHistoryEntity>>

    @Query("SELECT COUNT(*) FROM scan_history")
    fun getTotalScanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM scan_history WHERE riskLevel = 'DANGEROUS'")
    fun getDangerousCount(): Flow<Int>

    @Query("DELETE FROM scan_history")
    suspend fun deleteAll()
}

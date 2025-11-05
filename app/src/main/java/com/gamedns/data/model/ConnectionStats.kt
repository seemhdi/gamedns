package com.gamedns.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConnectionStats(
    val connectedDns: DnsServer? = null,
    val connectionStartTime: Long = 0,      // Unix timestamp when connection started
    val currentPing: Long = 0,              // Current ping in milliseconds
    val dataUsed: Long = 0,                 // Data used in bytes
    val isConnected: Boolean = false,
    val connectionDuration: Long = 0        // Duration in milliseconds
) : Parcelable {
    
    /**
     * Get formatted connection duration
     */
    fun getFormattedDuration(): String {
        val seconds = connectionDuration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        
        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }
    
    /**
     * Get formatted data usage
     */
    fun getFormattedDataUsage(): String {
        return when {
            dataUsed < 1024 -> "${dataUsed}B"
            dataUsed < 1024 * 1024 -> "${dataUsed / 1024}KB"
            dataUsed < 1024 * 1024 * 1024 -> "${dataUsed / (1024 * 1024)}MB"
            else -> "${dataUsed / (1024 * 1024 * 1024)}GB"
        }
    }
    
    /**
     * Get current connection quality
     */
    fun getConnectionQuality(): ConnectionQuality {
        if (!isConnected) return ConnectionQuality.DISCONNECTED
        
        return when {
            currentPing < 20 -> ConnectionQuality.EXCELLENT
            currentPing < 50 -> ConnectionQuality.GOOD
            currentPing < 100 -> ConnectionQuality.FAIR
            currentPing < 200 -> ConnectionQuality.POOR
            else -> ConnectionQuality.VERY_POOR
        }
    }
}

@Parcelize
enum class ConnectionQuality : Parcelable {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR,
    VERY_POOR,
    DISCONNECTED
}

/**
 * Connection state for UI
 */
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    data class Connected(val stats: ConnectionStats) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

package com.gamedns.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DnsTestResult(
    val dnsServerId: Int,
    val dnsServerName: String,
    val avgPing: Long,          // Average ping in milliseconds
    val minPing: Long,          // Minimum ping in milliseconds
    val maxPing: Long,          // Maximum ping in milliseconds
    val packetLoss: Float,      // Packet loss percentage (0-100)
    val timestamp: Long,        // Unix timestamp when test was performed
    val isSuccess: Boolean,     // Whether the test was successful
    val testCount: Int = 5      // Number of tests performed
) : Parcelable {
    
    /**
     * Get quality rating based on average ping
     */
    fun getQualityRating(): DnsQuality {
        return when {
            !isSuccess -> DnsQuality.FAILED
            avgPing < 20 -> DnsQuality.EXCELLENT
            avgPing < 50 -> DnsQuality.GOOD
            avgPing < 100 -> DnsQuality.FAIR
            avgPing < 200 -> DnsQuality.POOR
            else -> DnsQuality.VERY_POOR
        }
    }
    
    /**
     * Get formatted ping string
     */
    fun getFormattedPing(): String {
        return if (isSuccess) {
            "${avgPing}ms"
        } else {
            "Failed"
        }
    }
    
    /**
     * Get stability score (0-100) based on ping variance
     */
    fun getStabilityScore(): Int {
        if (!isSuccess) return 0
        
        val variance = maxPing - minPing
        return when {
            variance < 10 -> 100
            variance < 30 -> 80
            variance < 50 -> 60
            variance < 100 -> 40
            else -> 20
        }
    }
}

@Parcelize
enum class DnsQuality : Parcelable {
    EXCELLENT,      // < 20ms - Perfect for gaming
    GOOD,           // 20-50ms - Good for gaming
    FAIR,           // 50-100ms - Acceptable
    POOR,           // 100-200ms - Not recommended for gaming
    VERY_POOR,      // > 200ms - Very slow
    FAILED          // Test failed
}

/**
 * Test configuration for DNS speed testing
 */
data class DnsTestConfig(
    val testCount: Int = 5,              // Number of ping tests to perform
    val timeout: Long = 5000,            // Timeout in milliseconds
    val testDomain: String = "www.google.com",  // Domain to test
    val delayBetweenTests: Long = 100    // Delay between tests in milliseconds
)

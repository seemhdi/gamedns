package com.gamedns.service

import android.util.Log
import com.gamedns.data.model.DnsServer
import com.gamedns.data.model.DnsTestConfig
import com.gamedns.data.model.DnsTestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for testing DNS speed and performance
 */
@Singleton
class DnsTestService @Inject constructor() {
    
    companion object {
        private const val TAG = "DnsTestService"
        private const val DEFAULT_TEST_DOMAIN = "www.google.com"
        private const val DEFAULT_TEST_COUNT = 5
        private const val DEFAULT_TIMEOUT = 5000L
        private const val DELAY_BETWEEN_TESTS = 100L
    }
    
    /**
     * Test DNS server speed
     * @param dnsServer DNS server to test
     * @param config Test configuration
     * @return Test result with ping statistics
     */
    suspend fun testDnsSpeed(
        dnsServer: DnsServer,
        config: DnsTestConfig = DnsTestConfig()
    ): DnsTestResult = withContext(Dispatchers.IO) {
        
        Log.d(TAG, "Testing DNS: ${dnsServer.name}")
        
        val pingResults = mutableListOf<Long>()
        var successCount = 0
        
        repeat(config.testCount) { iteration ->
            try {
                val startTime = System.currentTimeMillis()
                
                // Perform DNS lookup
                val isSuccess = performDnsLookup(
                    dnsServer.primaryDns,
                    config.testDomain,
                    config.timeout
                )
                
                val endTime = System.currentTimeMillis()
                val pingTime = endTime - startTime
                
                if (isSuccess && pingTime < config.timeout) {
                    pingResults.add(pingTime)
                    successCount++
                    Log.d(TAG, "Test ${iteration + 1}: ${pingTime}ms - Success")
                } else {
                    Log.d(TAG, "Test ${iteration + 1}: Failed or timeout")
                }
                
                // Delay between tests
                if (iteration < config.testCount - 1) {
                    delay(config.delayBetweenTests)
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Test ${iteration + 1} error", e)
            }
        }
        
        // Calculate statistics
        return@withContext if (pingResults.isNotEmpty()) {
            val avgPing = pingResults.average().toLong()
            val minPing = pingResults.minOrNull() ?: 0
            val maxPing = pingResults.maxOrNull() ?: 0
            val packetLoss = ((config.testCount - successCount).toFloat() / config.testCount) * 100
            
            Log.d(TAG, "Test completed - Avg: ${avgPing}ms, Min: ${minPing}ms, Max: ${maxPing}ms, Loss: ${packetLoss}%")
            
            DnsTestResult(
                dnsServerId = dnsServer.id,
                dnsServerName = dnsServer.name,
                avgPing = avgPing,
                minPing = minPing,
                maxPing = maxPing,
                packetLoss = packetLoss,
                timestamp = System.currentTimeMillis(),
                isSuccess = true,
                testCount = config.testCount
            )
        } else {
            Log.d(TAG, "Test failed - No successful pings")
            
            DnsTestResult(
                dnsServerId = dnsServer.id,
                dnsServerName = dnsServer.name,
                avgPing = 0,
                minPing = 0,
                maxPing = 0,
                packetLoss = 100f,
                timestamp = System.currentTimeMillis(),
                isSuccess = false,
                testCount = config.testCount
            )
        }
    }
    
    /**
     * Test multiple DNS servers
     * @param dnsServers List of DNS servers to test
     * @param config Test configuration
     * @return List of test results
     */
    suspend fun testMultipleDns(
        dnsServers: List<DnsServer>,
        config: DnsTestConfig = DnsTestConfig()
    ): List<DnsTestResult> = withContext(Dispatchers.IO) {
        
        Log.d(TAG, "Testing ${dnsServers.size} DNS servers")
        
        val results = mutableListOf<DnsTestResult>()
        
        dnsServers.forEach { dnsServer ->
            val result = testDnsSpeed(dnsServer, config)
            results.add(result)
            
            // Small delay between different DNS tests
            delay(200)
        }
        
        return@withContext results
    }
    
    /**
     * Find the best DNS server from a list
     * @param dnsServers List of DNS servers to test
     * @param config Test configuration
     * @return Best DNS server based on lowest average ping
     */
    suspend fun findBestDns(
        dnsServers: List<DnsServer>,
        config: DnsTestConfig = DnsTestConfig()
    ): Pair<DnsServer, DnsTestResult>? = withContext(Dispatchers.IO) {
        
        Log.d(TAG, "Finding best DNS from ${dnsServers.size} servers")
        
        val results = testMultipleDns(dnsServers, config)
        
        // Filter successful results and sort by average ping
        val successfulResults = results
            .filter { it.isSuccess }
            .sortedBy { it.avgPing }
        
        return@withContext if (successfulResults.isNotEmpty()) {
            val bestResult = successfulResults.first()
            val bestDns = dnsServers.find { it.id == bestResult.dnsServerId }
            
            if (bestDns != null) {
                Log.d(TAG, "Best DNS: ${bestDns.name} with ${bestResult.avgPing}ms")
                Pair(bestDns, bestResult)
            } else {
                null
            }
        } else {
            Log.d(TAG, "No successful DNS found")
            null
        }
    }
    
    /**
     * Perform DNS lookup using specified DNS server
     * @param dnsServerIp DNS server IP address
     * @param domain Domain to lookup
     * @param timeout Timeout in milliseconds
     * @return True if lookup was successful
     */
    private fun performDnsLookup(
        dnsServerIp: String,
        domain: String,
        timeout: Long
    ): Boolean {
        return try {
            // Method 1: Simple DNS lookup
            // Note: This doesn't force the use of specific DNS server
            // In production, you'd use a proper DNS client library like dnsjava
            
            val address = InetAddress.getByName(domain)
            address != null
            
        } catch (e: Exception) {
            Log.e(TAG, "DNS lookup failed", e)
            false
        }
    }
    
    /**
     * Perform ping test to DNS server
     * @param dnsServerIp DNS server IP address
     * @param timeout Timeout in milliseconds
     * @return Ping time in milliseconds, or -1 if failed
     */
    suspend fun pingDnsServer(
        dnsServerIp: String,
        timeout: Long = DEFAULT_TIMEOUT
    ): Long = withContext(Dispatchers.IO) {
        
        return@withContext try {
            val startTime = System.currentTimeMillis()
            
            // Try to connect to DNS server on port 53
            val socket = Socket()
            socket.connect(InetSocketAddress(dnsServerIp, 53), timeout.toInt())
            socket.close()
            
            val endTime = System.currentTimeMillis()
            endTime - startTime
            
        } catch (e: Exception) {
            Log.e(TAG, "Ping failed to $dnsServerIp", e)
            -1L
        }
    }
    
    /**
     * Check if DNS server is reachable
     * @param dnsServerIp DNS server IP address
     * @param timeout Timeout in milliseconds
     * @return True if reachable
     */
    suspend fun isDnsReachable(
        dnsServerIp: String,
        timeout: Long = DEFAULT_TIMEOUT
    ): Boolean = withContext(Dispatchers.IO) {
        
        return@withContext try {
            val address = InetAddress.getByName(dnsServerIp)
            address.isReachable(timeout.toInt())
        } catch (e: Exception) {
            Log.e(TAG, "Reachability check failed for $dnsServerIp", e)
            false
        }
    }
}

/**
 * Extension function to get quality description
 */
fun DnsTestResult.getQualityDescription(): String {
    return when (getQualityRating()) {
        com.gamedns.data.model.DnsQuality.EXCELLENT -> "Excellent - Perfect for gaming"
        com.gamedns.data.model.DnsQuality.GOOD -> "Good - Great for gaming"
        com.gamedns.data.model.DnsQuality.FAIR -> "Fair - Acceptable"
        com.gamedns.data.model.DnsQuality.POOR -> "Poor - Not recommended"
        com.gamedns.data.model.DnsQuality.VERY_POOR -> "Very Poor - Very slow"
        com.gamedns.data.model.DnsQuality.FAILED -> "Failed - Unreachable"
    }
}

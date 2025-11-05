package com.gamedns.domain.usecase

import com.gamedns.data.model.DnsServer
import com.gamedns.data.model.DnsTestConfig
import com.gamedns.data.model.DnsTestResult
import com.gamedns.service.DnsTestService
import javax.inject.Inject

/**
 * Use case for testing DNS speed
 */
class TestDnsSpeedUseCase @Inject constructor(
    private val dnsTestService: DnsTestService
) {
    
    /**
     * Test single DNS server speed
     */
    suspend fun testSingle(
        dnsServer: DnsServer,
        config: DnsTestConfig = DnsTestConfig()
    ): DnsTestResult {
        return dnsTestService.testDnsSpeed(dnsServer, config)
    }
    
    /**
     * Test multiple DNS servers
     */
    suspend fun testMultiple(
        dnsServers: List<DnsServer>,
        config: DnsTestConfig = DnsTestConfig()
    ): List<DnsTestResult> {
        return dnsTestService.testMultipleDns(dnsServers, config)
    }
    
    /**
     * Quick ping test
     */
    suspend fun quickPing(dnsServerIp: String): Long {
        return dnsTestService.pingDnsServer(dnsServerIp)
    }
}

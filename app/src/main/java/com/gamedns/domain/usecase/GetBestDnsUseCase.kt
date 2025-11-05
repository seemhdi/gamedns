package com.gamedns.domain.usecase

import com.gamedns.data.model.DnsServer
import com.gamedns.data.model.DnsTestConfig
import com.gamedns.data.model.DnsTestResult
import com.gamedns.service.DnsTestService
import javax.inject.Inject

/**
 * Use case for finding the best DNS server
 */
class GetBestDnsUseCase @Inject constructor(
    private val dnsTestService: DnsTestService
) {
    
    /**
     * Find best DNS from a list of servers
     * @param dnsServers List of DNS servers to test
     * @param config Test configuration
     * @return Pair of best DNS server and its test result, or null if none found
     */
    suspend fun findBest(
        dnsServers: List<DnsServer>,
        config: DnsTestConfig = DnsTestConfig()
    ): Pair<DnsServer, DnsTestResult>? {
        return dnsTestService.findBestDns(dnsServers, config)
    }
    
    /**
     * Find best DNS for gaming (low latency)
     * @param dnsServers List of DNS servers to test
     * @return Best gaming DNS or null
     */
    suspend fun findBestForGaming(
        dnsServers: List<DnsServer>
    ): Pair<DnsServer, DnsTestResult>? {
        // Use fewer tests but more frequent for gaming
        val config = DnsTestConfig(
            testCount = 3,
            timeout = 3000,
            delayBetweenTests = 50
        )
        
        return findBest(dnsServers, config)
    }
}

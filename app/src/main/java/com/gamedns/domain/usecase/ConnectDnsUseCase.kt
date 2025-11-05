package com.gamedns.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.VpnService
import com.gamedns.data.model.DnsServer
import com.gamedns.service.GameDnsVpnService
import javax.inject.Inject

/**
 * Use case for connecting to a DNS server via VPN
 */
class ConnectDnsUseCase @Inject constructor(
    private val context: Context
) {
    
    /**
     * Prepare VPN service
     * @return Intent to request VPN permission, or null if already prepared
     */
    fun prepareVpn(): Intent? {
        return VpnService.prepare(context)
    }
    
    /**
     * Connect to DNS server
     * @param dnsServer DNS server to connect to
     */
    fun connect(dnsServer: DnsServer) {
        val intent = Intent(context, GameDnsVpnService::class.java).apply {
            action = GameDnsVpnService.ACTION_CONNECT
            putExtra(GameDnsVpnService.EXTRA_DNS_SERVER, dnsServer)
        }
        context.startService(intent)
    }
    
    /**
     * Disconnect from current DNS
     */
    fun disconnect() {
        val intent = Intent(context, GameDnsVpnService::class.java).apply {
            action = GameDnsVpnService.ACTION_DISCONNECT
        }
        context.startService(intent)
    }
    
    /**
     * Check if VPN is currently connected
     */
    fun isConnected(): Boolean {
        return GameDnsVpnService.isRunning
    }
    
    /**
     * Get currently connected DNS server
     */
    fun getCurrentDns(): DnsServer? {
        return GameDnsVpnService.currentDnsServer
    }
}

package com.gamedns.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DnsServer(
    val id: Int,
    val name: String,
    val primaryDns: String,
    val secondaryDns: String,
    val icon: String = "",
    val category: DnsCategory,
    val description: String = "",
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val lastTestResult: DnsTestResult? = null
) : Parcelable

@Parcelize
enum class DnsCategory : Parcelable {
    GLOBAL,      // Google, Cloudflare, OpenDNS, etc.
    IRAN,        // Shecan, 403, Electro, etc.
    GAMING,      // Gaming-optimized DNS
    CUSTOM       // User-added DNS servers
}

object PredefinedDnsServers {
    
    val allServers = listOf(
        // Global DNS Servers
        DnsServer(
            id = 1,
            name = "Google DNS",
            primaryDns = "8.8.8.8",
            secondaryDns = "8.8.4.4",
            icon = "google",
            category = DnsCategory.GLOBAL,
            description = "Fast and reliable DNS by Google"
        ),
        DnsServer(
            id = 2,
            name = "Cloudflare DNS",
            primaryDns = "1.1.1.1",
            secondaryDns = "1.0.0.1",
            icon = "cloudflare",
            category = DnsCategory.GLOBAL,
            description = "Privacy-focused DNS with great speed"
        ),
        DnsServer(
            id = 3,
            name = "OpenDNS",
            primaryDns = "208.67.222.222",
            secondaryDns = "208.67.220.220",
            icon = "opendns",
            category = DnsCategory.GLOBAL,
            description = "Secure DNS with content filtering"
        ),
        DnsServer(
            id = 4,
            name = "Quad9",
            primaryDns = "9.9.9.9",
            secondaryDns = "149.112.112.112",
            icon = "quad9",
            category = DnsCategory.GLOBAL,
            description = "Security-focused DNS blocking malicious domains"
        ),
        DnsServer(
            id = 5,
            name = "AdGuard DNS",
            primaryDns = "94.140.14.14",
            secondaryDns = "94.140.15.15",
            icon = "adguard",
            category = DnsCategory.GLOBAL,
            description = "DNS with ad blocking capabilities"
        ),
        
        // Iran DNS Servers
        DnsServer(
            id = 6,
            name = "Shecan",
            primaryDns = "178.22.122.100",
            secondaryDns = "185.51.200.2",
            icon = "shecan",
            category = DnsCategory.IRAN,
            description = "Popular Iranian DNS service"
        ),
        DnsServer(
            id = 7,
            name = "403 Online",
            primaryDns = "10.202.10.202",
            secondaryDns = "10.202.10.102",
            icon = "403",
            category = DnsCategory.IRAN,
            description = "Fast Iranian DNS service"
        ),
        DnsServer(
            id = 8,
            name = "Electro",
            primaryDns = "78.157.42.100",
            secondaryDns = "78.157.42.101",
            icon = "electro",
            category = DnsCategory.IRAN,
            description = "Reliable Iranian DNS"
        ),
        DnsServer(
            id = 9,
            name = "Radar Game",
            primaryDns = "10.202.10.10",
            secondaryDns = "10.202.10.11",
            icon = "radargame",
            category = DnsCategory.IRAN,
            description = "Gaming-optimized Iranian DNS"
        ),
        
        // Gaming DNS Servers
        DnsServer(
            id = 10,
            name = "Cloudflare Gaming",
            primaryDns = "1.1.1.1",
            secondaryDns = "1.0.0.1",
            icon = "cloudflare_gaming",
            category = DnsCategory.GAMING,
            description = "Low latency DNS for gaming"
        ),
        DnsServer(
            id = 11,
            name = "Google Gaming",
            primaryDns = "8.8.8.8",
            secondaryDns = "8.8.4.4",
            icon = "google_gaming",
            category = DnsCategory.GAMING,
            description = "Optimized Google DNS for games"
        )
    )
    
    fun getByCategory(category: DnsCategory): List<DnsServer> {
        return allServers.filter { it.category == category }
    }
    
    fun getById(id: Int): DnsServer? {
        return allServers.find { it.id == id }
    }
}
